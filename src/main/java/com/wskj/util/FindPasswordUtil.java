package com.wskj.util;

import java.sql.*;

/**
 * Created by youjm on 2015/7/14.
 */
public class FindPasswordUtil {
    public Connection getCon() throws Exception {
        Class.forName("org.postgresql.Driver").newInstance();
        String url = "jdbc:postgresql://localhost/ods";
        String user = "admin";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    //插入到数据库表findpassword  中
    public int insertInfor(String email, Timestamp date, String signature) throws Exception {
        Connection con = getCon();
        String sql = "insert into findpasswordtable(email,outdate,validkey) values(?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);
        pstmt.setTimestamp(2, date);
        pstmt.setString(3, signature);
        int res = pstmt.executeUpdate();
        pstmt.close();
        con.close();
        return res;
    }

    //找回密码，查询是否可以修改密码
    public boolean isChangePass(String email, String validkey) throws Exception {
        Connection con = getCon();
        String sql = "select * from findpasswordtable where email = ?";

        PreparedStatement pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        pstmt.setString(1, email);
        ResultSet res = pstmt.executeQuery();
        //用户名正确
        if (res.last()) {
            //验证码不对
            String signature = res.getString("validkey");
            if (!validkey.equals(signature)) {
                pstmt.close();
                con.close();
                return false;
            } else {
                //验证码正确，时间超时
                long current = System.currentTimeMillis();
                long time = res.getTimestamp("outdate").getTime();
                if (current > time) {
                    pstmt.close();
                    con.close();
                    return false;
                } else {
                    pstmt.close();
                    con.close();
                    return true;
                }
            }
        } else {
            pstmt.close();
            con.close();
            return false;
        }
    }
}
