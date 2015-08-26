package com.wskj.dao;

/**
 * Created by zhuangjy on 2015/7/7.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.wskj.dao.handler.UserMapper;
import com.wskj.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void create(String username, String password, String location) {
        String sql = "INSERT INTO ods.user(user_name, user_pass, location) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, username, password, location);
        return;
    }

    public User getUser(Integer id) {
        String sql = "select * from ods.user where user_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql,
                    new Object[]{id}, new UserMapper());
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User getUser(String username) {
        String sql = "select * from ods.user where user_name =?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserMapper());
            System.out.println("找到" + user.getUserName());
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getUserName(int userId) {
        String sql = "select user_name from ods.user where user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, String.class);
    }

    public List<User> listUsers() {
        String sql = "select * from ods.user";
        List<User> users = jdbcTemplate.query(sql,
                new UserMapper());
        return users;
    }

    public void delete(Integer id) {
        String sql = "delete from ods.user where user_id = ?";
        jdbcTemplate.update(sql, id);
        System.out.println("Deleted Record with ID = " + id);
        return;
    }

    public void resetPassWord(String userName, String passWord) {
        String sql = "update ods.user set user_pass=? where user_name=?";
        jdbcTemplate.update(sql, passWord, userName);
        return;
    }


    //update password
    public void update(Integer id, String password) {
        String sql = "update ods.user set user_pass = ? where user_id = ?";
        jdbcTemplate.update(sql, password, id);
        System.out.println("Updated Record with ID = " + id);
        return;
    }

    //判断密码是否正确 正确返回true
    public User judgeAccount(String username, String password) {
        try {
            User user = getUser(username);
            if (user == null || !user.getPassWord().equals(password))//密码错误
                return null;
            else
                return user;
        } catch (EmptyResultDataAccessException e) { //为空找不到该用户
            return null;
        }
    }

    /**
     * 注册使用
     */
    //插入待激活表
    public void insertAct(String username, String password, String signature, String location, Timestamp ts) {
        String sql = "INSERT INTO ods.test_register(user_name, user_pass, location, valid_key, out_date) VALUES ( ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, username, password, location, signature, ts);
        System.out.println("创建成功!");
        return;
    }

    //判断是否激活成功 可以的话激活
    public boolean isAct(String username, final String validkey) {
        User user = getUser(username);
        //如果不为空则返回错误
        if (user != null)
            return false;
        String sql = "SELECT id, user_name, user_pass, location, valid_key, out_date FROM ods.test_register where user_name= ? ORDER BY id DESC";
        Boolean judge = jdbcTemplate.query(sql, new Object[]{username}, new ResultSetExtractor<Boolean>() {
            public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    //判断是否过期
                    long outdate = rs.getTimestamp("out_date").getTime();
                    if (System.currentTimeMillis() > outdate)
                        return false;

                    //判断数字证书是否一致
                    String signature = rs.getString("valid_key");
                    if (!signature.equals(validkey))
                        return false;
                    //成功，可以插入该用户激活该账户
                    create(rs.getString("user_name"), rs.getString("user_pass"), rs.getString("location"));
                    return true;

                } else
                    return false; //没有找到该账户
            }
        });
        return judge;
    }


    //插入到数据库表findpassword中
    public void insertInfor(String email, Timestamp date, String signature) {
        String sql = "insert into ods.findpassword(email,out_date,valid_key) values(?,?,?)";
        jdbcTemplate.update(sql, email, date, signature);
        System.out.println("插入到数据库表findpassword中");
        return;
    }

    //判断是否有权限修改密码
    public boolean isChangePass(String email, final String validKey) {
        String sql = "select * from ods.findpassword where email =?  order by out_date desc";
        return jdbcTemplate.query(sql, new Object[]{email}, new ResultSetExtractor<Boolean>() {
            public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    //判断是否过期
                    long outdate = rs.getTimestamp("out_date").getTime();
                    if (System.currentTimeMillis() > outdate) {
                        System.out.println("过期");
                        return false;
                    }

                    //判断数字证书是否一致
                    String signature = rs.getString("valid_key");
                    if (!signature.equals(validKey)) {
                        System.out.println("证书不一致");
                        return false;
                    }
                    //成功，可以修改密码
                    return true;
                } else
                    return false;
            }
        });
    }
}
