package com.wskj.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.wskj.domain.User;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created by zhuangjy on 2015/7/7.
 */
public class UserMapper implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUserName(rs.getString("user_name"));
        user.setPassWord(rs.getString("user_pass"));
        user.setLocation(rs.getString("location"));
        return user;
    }
}
