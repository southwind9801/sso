package com.southwind.utils;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {
    public static String sendHttpRequest(String httpURL, Map<String,String> params){
        try {
            //1、定义URL
            URL url = new URL(httpURL);
            //2、开启连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //3、请求类型
            httpURLConnection.setRequestMethod("POST");
            //4、带参数
            httpURLConnection.setDoOutput(true);
            //5、拼参数
            if(params!=null && params.size()>0){
                StringBuffer stringBuffer = new StringBuffer();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    stringBuffer.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
                //6、删掉最前面的 &
                httpURLConnection.getOutputStream().write(stringBuffer.substring(1).toString().getBytes("UTF-8"));
            }
            //7、发送请求
            httpURLConnection.connect();
            //8、接收响应
            String response = StreamUtils.copyToString(httpURLConnection.getInputStream(), Charset.forName("UTF-8"));
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
