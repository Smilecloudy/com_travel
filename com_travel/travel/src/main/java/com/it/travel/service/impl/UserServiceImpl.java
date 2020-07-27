package com.it.travel.service.impl;

import com.it.travel.dao.UserDao;
import com.it.travel.dao.impl.UserDaoImpl;
import com.it.travel.domain.User;
import com.it.travel.service.UserService;
import com.it.travel.util.MailUtils;
import com.it.travel.util.Md5Utils;
import com.it.travel.util.UuidUtil;

/**
 * @auther: cyb
 * @create: 2020/7/26 15:59
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        User u = userDao.findByUser(user.getUsername());
        if (u != null) {
            //用户名重复
            return false;
        }
        //保存用户信息
        //设置密码加密
        try {
            String passwordMd5 = Md5Utils.encodeByMd5(user.getPassword());
            user.setPassword(passwordMd5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //发送激活邮箱
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(),content,"旅游网激活邮件");
        return true;
    }

    @Override
    public boolean active(String code) {
        //根据激活码查询用户
        User u = userDao.findByCode(code);
        if (u == null) {
            userDao.updateStatus(u);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        String password = user.getPassword();
        try {
            String passwordMd5 = Md5Utils.encodeByMd5(password);
            user.setPassword(passwordMd5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User u = userDao.findByUserNameAndPassWord(user.getUsername(), user.getPassword());
        return u;
    }
}
