package com.it.travel.service;

import com.it.travel.domain.User;

/**
 * @auther: cyb
 * @create: 2020/7/26 15:58
 */
public interface UserService {
    boolean regist(User user);

    boolean active(String code);

    User login(User user);
}
