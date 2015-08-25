package com.wskj.service;

import com.wskj.dao.UserDao;
import com.wskj.domain.User;
import com.wskj.util.GetTime;
import com.wskj.util.MD5Util;
import com.wskj.util.SimpleMailSender;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Random;

/**
 * Created by zhuangjy on 2015/8/25.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User judgeAccount(User user) {
        return userDao.judgeAccount(user.getUserName(), user.getPassWord());
    }

    public User getUser(String userName) {
        return userDao.getUser(userName);
    }

    public User getUser(int userId) {
        return userDao.getUser(userId);
    }

    public boolean register(User user, HttpServletRequest request) {
        SimpleMailSender sms = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
        Timestamp ts = GetTime.getOutTime();
        Random random = new Random();
        String key = user.getUserName() + "|" + ts + "|" + random.nextInt();
        String signature = MD5Util.MD5(key);
        userDao.insertAct(user.getUserName(), user.getPassWord(), signature, user.getLocation(), ts);
        String absoluteContextPath = (String) request.getAttribute("absoluteContextPath");
        String url = absoluteContextPath + "/activateAccount" + "?uid=" + user.getUserName() + "&amp;validkey=" + signature;
        try {
            sms.send(user.getUserName(), "欢迎注册ods系统", "<p>恭喜您已经成功注册帐号,<a href='" + url +
                    "'>点击激活帐号</a> </p> <p>如果不是您本人操作,请忽略此消息</p>");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isActable(String uid, String validkey) {
        return userDao.isAct(uid, validkey);
    }


    public boolean findPassword(String userName, HttpServletRequest request) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        User user = userDao.getUser(userName);
        if (user == null)
            return false;
        SimpleMailSender sms = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
        Timestamp outTime = GetTime.getOutTime();
        Random random = new Random();
        String key = user.getUserName() + "|" + outTime + "|" + random.nextInt();
        String signature = MD5Util.MD5(key);
        userDao.insertInfor(userName, outTime, signature);
        String absoluteContextPath = (String) request.getAttribute("absoluteContextPath");
        String url = absoluteContextPath + "/gotoResetPassword" + "?userName=" + userName + "&amp;validKey=" + signature;
        try {
            sms.send(user.getUserName(), "ods找回密码", "<p>恭喜您修改密码成功,请在两分钟之内<a href='" + url + "'>点击修改密码</a></p>" +
                    "<p><font color='red'>注:如果不是您本人操作,请忽略该消息。</font></p>");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean changePassAble(String userName, String vaildKey) {
        return userDao.isChangePass(userName, vaildKey);
    }

    public void resetPassWord(String userName, String passWord) {
        userDao.resetPassWord(userName, passWord);
    }

    public void loginOut(HttpSession session, HttpServletRequest request) {
        session.removeAttribute("curUser");
        request.removeAttribute("userName");
        request.removeAttribute("passWord");
    }


}
