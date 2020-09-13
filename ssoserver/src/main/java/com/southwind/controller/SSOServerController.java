package com.southwind.controller;

import com.southwind.mock.MockDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@Slf4j
public class SSOServerController {

    @RequestMapping("/checkToken")
    public String checkToken(String redirectUrl, HttpServletRequest request, HttpSession session, Model model){
        //获取令牌
        String token = (String) session.getServletContext().getAttribute("token");
        if(StringUtils.isEmpty(token)){
            //令牌为空，则登录
            model.addAttribute("redirectUrl", redirectUrl);
            return "login";
        }else{
            //验证令牌
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if(cookie.getValue().equals(token)){
                    //验证通过，把令牌返回客户端
                    return "redirect:"+redirectUrl+"?token="+token;
                }
            }
            //验证不通过，则登录
            model.addAttribute("redirectUrl", redirectUrl);
            return "login";
        }
    }

    @PostMapping("/login")
    public String login(String username,String password,String redirectUrl,HttpSession session,Model model){
        //判断登录
        if("admin".equals(username) && "123123".equals(password)){
            //1、创建令牌
            String token = UUID.randomUUID().toString();
            log.info("令牌创建成功，token={}", token);
            //2、令牌保存到全局session
            session.getServletContext().setAttribute("token", token);
            //3、令牌保存到数据库
            MockDB.tokenSet.add(token);
            //4、把令牌返回给用户
            return "redirect:"+redirectUrl+"?token="+token;
        }else{
            log.info("用户名或密码错误！username={},password={}", username,password);
            model.addAttribute("redirectUrl", redirectUrl);
            return "login";
        }
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token,String clientLogoutUrl){
        if(MockDB.tokenSet.contains(token)){
            log.info("验证通过！token={}", token);
            //把clientLogoutUrl保存到数据库
            Set<String> set = MockDB.clientLogoutUrlMap.get(token);
            if(set == null){
                set = new HashSet<>();
                MockDB.clientLogoutUrlMap.put(token, set);
            }
            set.add(clientLogoutUrl);
            return "true";
        }
        return "false";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }
}
