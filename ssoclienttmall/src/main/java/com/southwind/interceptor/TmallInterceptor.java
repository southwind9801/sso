package com.southwind.interceptor;

import com.southwind.utils.HttpUtil;
import com.southwind.utils.SSOClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Slf4j
public class TmallInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、判断是否为登录状态
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if(isLogin!=null && isLogin){
            return true;
        }

        //2、判断token
        String token = request.getParameter("token");
        //token不为空
        if(!StringUtils.isEmpty(token)){
            log.info("token存在，需要验证，token={}", token);
            //发起验证
            String httpUrl = SSOClientUtil.SERVER_HOST_URL+"/verify";
            HashMap<String,String> params = new HashMap<>();
            params.put("token", token);
            params.put("clientLogoutUrl",SSOClientUtil.getClientLogoutUrl());
            String isVerify = HttpUtil.sendHttpRequest(httpUrl, params);
            if("true".equals(isVerify)){
                log.info("token验证通过，token={}", token);
                //token保存到本地Cookie
                Cookie cookie = new Cookie("token", token);
                response.addCookie(cookie);
                session.setAttribute("isLogin", true);
                return true;
            }
        }
        //3、token为空，跳转到认证中心进行登录
        SSOClientUtil.redirectToCheckToken(request, response);
        return false;
    }
}
