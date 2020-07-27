package com.it.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.travel.domain.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @auther: cyb
 * @create: 2020/7/26 15:20
 */
/*
    这个类主要是完成方法的分发操作，作为基类存在
    另实现了两个功能：  1 将传入的对象序列化并返回客户端。其实质是：将对象转为json字符串，
                        并将json数据填充到字节输出流中。
                      2 将传入的对象序列化并返回。其实质是将对象转为json字符串。
 */
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);

        try {
            //3.获取方法对象Method
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            //暴力反射
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     */
    public void writeValue( Object o, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(o);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(s);
    }

    /**
     * 将传入的对象序列化为json，返回
     */
    public String writeValueAsString(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }


}

