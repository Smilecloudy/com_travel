package com.it.travel.dao.impl;

import com.it.travel.dao.UserDao;
import com.it.travel.domain.User;
import com.it.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @auther: cyb
 * @create: 2020/7/26 16:19
 */
public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUser(String username) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE username = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return user;
    }

    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE CODE = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User u) {
        String sql = "UPDATE tab_user SET STATUS = 'Y' WHERE uid = ?";
        template.update(sql, u.getUid());
    }

    @Override
    public void save(User user) {
        //1.定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //2.执行sql
        template.update(sql, user.getUsername(),
                user.getPassword(), user.getName()
                , user.getBirthday(), user.getSex(),
                user.getTelephone(), user.getEmail(),
                user.getStatus(), user.getCode());

    }

    @Override
    public User findByUserNameAndPassWord(String username, String password) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE usernam = ? AND PASSWORD = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username, password);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return user;
    }
}
