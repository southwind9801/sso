package com.southwind.listener;

import com.southwind.mock.MockDB;
import com.southwind.utils.HttpUtil;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Iterator;
import java.util.Set;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String token = (String) se.getSession().getServletContext().getAttribute("token");
        //删除全局会话中的token
        se.getSession().getServletContext().removeAttribute("token");
        //删除数据库中的token
        MockDB.tokenSet.remove(token);
        //通知所有客户端销毁session
        Set<String> set = MockDB.clientLogoutUrlMap.get(token);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            HttpUtil.sendHttpRequest(iterator.next(), null);
        }
        //删除数据库中的客户端登出URL集合
        MockDB.clientLogoutUrlMap.remove(token);
    }
}
