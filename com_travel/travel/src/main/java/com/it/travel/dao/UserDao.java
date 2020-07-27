package com.it.travel.dao;

import com.it.travel.domain.User;

/**
 * @auther: cyb
 * @create: 2020/7/26 16:18
 */
public interface UserDao {

    //查询数据库是否有这个username
    User findByUser(String username);

    //根据code查询用户
    User findByCode(String code);

    //更新激活状态
    void updateStatus(User u);

    //注册用户
    void save(User user);

    //根据用户名和密码查询用户信息
    User findByUserNameAndPassWord(String username, String password);
}
