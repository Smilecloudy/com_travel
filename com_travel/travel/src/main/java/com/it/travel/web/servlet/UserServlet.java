package com.it.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.travel.domain.ResultInfo;
import com.it.travel.domain.User;
import com.it.travel.service.UserService;
import com.it.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @auther: cyb
 * @create: 2020/7/26 15:39
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();

    /**
     * 注册功能
     */
    protected void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证校验
        String check = request.getParameter("check");
        //从session中获取验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        //为保证验证码只使用一次，将这个session移除
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        //比较验证码是否输入正确
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(checkcode_server)) {
            //验证码输入错误，构建返回结果
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码输入错误");
            //将resultInfo序列化为JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(objectMapper);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //登录成功
        //获取数据
        Map<String, String[]> map = request.getParameterMap();
        //封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用userService完成注册
        boolean regist = userService.regist(user);
        ResultInfo resultInfo = new ResultInfo();
        //判断响应结果
        if (regist) {
            resultInfo.setFlag(true);
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败");
        }
        //将info对象序列化为json,然后返回，这个方法已经在BaseServlet中封装好了
        writeValue(resultInfo, response);
    }

    /**
     * 激活功能
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //调用service激活
            boolean active = userService.active(code);
            //判断标记
            String msg = "";
            if (active) {
                //激活成功
                msg = "激活成功，请<a href='login.html'>登录</a>";
            } else {
                msg = "激活失败，请联系管理员";
            }
            writeValue(msg, response);
        }
    }

    /**
     * 登录功能
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        //将数据封装到user中
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User u = userService.login(user);
        //构建返回结果
        ResultInfo info = new ResultInfo();
        //判断u是否为空
        if (u == null) {
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请去邮箱激活");
        }
        //判断是否登录成功
        if (u != null && "Y".equals(u.getStatus())) {
            info.setFlag(true);
            //登录成功，把user对象存放到session域中
            request.getSession().setAttribute("user",u);
        }
        writeValue(info, response);
    }

    /**
     * 退出功能
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session
        request.getSession().invalidate();
        //跳转到登录页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 查询单个对象
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取登陆人的名字
        Object user = request.getSession().getAttribute("user");
        writeValue(user,response);
    }
}
