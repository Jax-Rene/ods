package com.wskj.web;

import com.wskj.dao.UserDao;
import com.wskj.model.User;
import com.wskj.tools.GetTime;
import com.wskj.tools.MD5Util;
import com.wskj.tools.SimpleMailSender;
import com.wskj.tools.VerifyCodeUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Random;

/**
 * Created by zhuangjy on 2015/7/7.
 */
@Controller
@SessionAttributes("curUser")
public class LoginController {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
    UserDao userDao = (UserDao) ctx.getBean("userDao");

    @RequestMapping(value = "/gotoLogin", method = RequestMethod.GET)
    public String returnLogin() {
        return "login";
    }

    @RequestMapping(value = "/inputLogin", method = RequestMethod.POST)
    public String inputLogin(@ModelAttribute User user, ModelMap model, HttpSession session,
                             String autoLogin, String checkCode) {
        //自动登录
        User curUser = (User) session.getAttribute("curUser");
        if (curUser != null) {
            user = userDao.judgeAccount(curUser.getUserName(), curUser.getPassWord());
            if (user != null)
                return "redirect:/gotoIndex";
            else {
                model.addAttribute("error", "登录超时,请重新登录");
                return "login";
            }
        }

        String rightVerifyCode = (String) session.getAttribute("randCheckCode");
        if (!checkCode.equalsIgnoreCase(rightVerifyCode)) {
            model.addAttribute("error", "验证码出错!");
            model.addAttribute("userName",user.getUserName());
            model.addAttribute("passWord",user.getPassWord());
            return "login";
        }
        user = userDao.judgeAccount(user.getUserName(), user.getPassWord());
        if (user != null) {
            //判断下次是否需要自动登录
            session.setAttribute("curUser",user);
            if (autoLogin != "") {
                session.setMaxInactiveInterval(2592000);
            }
            return "redirect:/gotoIndex";
        } else {
            model.addAttribute("error", "用户名、密码错误请重新输入!");
            model.addAttribute("userName",user.getUserName());
            model.addAttribute("passWord",user.getPassWord());
        }
        return "login";
    }

    @RequestMapping(value = "/gotoIndex" , method = RequestMethod.GET)
    public String gotoIndex(){
        return "index";
    }

    @RequestMapping(value = "/inputRegister", method = RequestMethod.POST)
    public String Register(@ModelAttribute User user, String passWord2,ModelMap model,HttpServletRequest request) {
        User t = userDao.getUser(user.getUserName());
        if (t != null) {
            model.addAttribute("error", "邮箱已被注册,请登录!");
            return "login";
        }
        if(!user.getPassWord().equals(passWord2)){
            model.addAttribute("error","两次输入密码不一致!");
            return "login";
        }
        SimpleMailSender sms = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
        String recipient = user.getUserName();
        Timestamp ts = GetTime.getOutTime();
        Random random = new Random();
        String key = user.getUserName() + "|" + ts + "|" + random.nextInt();
        String signature = MD5Util.MD5(key);
        userDao.insertAct(user.getUserName(), user.getPassWord(), signature, user.getLocation(), ts);
        String absoluteContextPath = (String) request.getAttribute("absoluteContextPath");
        String url = absoluteContextPath + "/activateAccount" + "?uid=" + user.getUserName() + "&amp;validkey=" + signature;

        try {
            sms.send(recipient, "欢迎注册ods系统", "<p>恭喜您已经成功注册帐号,<a href='" + url  +
                    "'>点击激活帐号</a> </p> <p>如果不是您本人操作,请忽略此消息</p>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("content", "请登录邮箱点击链接激活该账户!该页面将在5秒后自动关闭...");
        return "transient";
    }

    @RequestMapping(value = "/activateAccount", method = RequestMethod.GET)
    public String activateAccount(String uid, String validkey, ModelMap model,HttpSession session) {
        boolean judge = userDao.isAct(uid, validkey);
        if (judge) {
            User curUser = userDao.getUser(uid);
            session.setAttribute("curUser",curUser);
            return "redirect:/gotoIndex";
        } else {
            model.addAttribute("content", "激活失败,请重新注册!");
            return "transient";
        }
    }

    //获取验证码
    @RequestMapping(value = "/PictureCheckCode", method = RequestMethod.GET)
    public void PictureCheckCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VerifyCodeUtil verifycode = new VerifyCodeUtil();
        verifycode.VerifyCode(request, response);
    }


    /**
     * 用于找回密码
     */

    @RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
    public ModelAndView returnFindPassword() {
        return new ModelAndView("findpassword");
    }

    @RequestMapping(value = "/findPassword", method = RequestMethod.GET)
    public String findPassword(String userName, ModelMap model) throws MessagingException {
        //判断用户是否存在
        User user = userDao.getUser(userName);
        if (user != null) {
            SimpleMailSender sms = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
            String recipient = user.getUserName();
            java.sql.Timestamp ts = GetTime.getOutTime();
            Random random = new Random();
            String key = user.getUserName() + "|" + ts + "|" + random.nextInt();
            String signature = MD5Util.MD5(key);
            //插入数据库表
            userDao.insertInfor(userName, ts, signature);
            String url = "http://localhost:8080/gotoResetPassword" + "?userName=" + userName + "&amp;validKey=" + signature;
            sms.send(recipient, "ods找回密码", url);
            model.addAttribute("content", "请登录邮箱点击邮箱链接认证身份,本页面将在5秒后转向首页!");
            return "transient";
        } else {
            model.addAttribute("error", "不存在的账号,请注册!");
            return "findpassword";
        }
    }

    //关于找回密码的页面   ：输入两次新密码
    @RequestMapping(value = "/gotoResetPassword", method = RequestMethod.GET)
    public String gotoResetPassword(String userName, String validKey, ModelMap model) {
        //判断是否有权限修改密码
        if (userDao.isChangePass(userName, validKey)) {
            model.addAttribute("username", userName);
            return "resetpassword";
        } else {
            model.addAttribute("error", "您输入的链接有误");
            return "login";
        }
    }

    //重置密码
    @RequestMapping(value = "/resetPassWord", method = RequestMethod.POST)
    public String resetPassWord(String userName, String passWord, ModelMap model) {
        userDao.resetPassWord(userName, passWord);
        model.addAttribute("content", "恭喜您修改密码成功,请重新登录,该页面将在5秒后关闭!");
        return "transient";
    }

    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public String loginOut(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.removeAttribute("curUser");
        request.removeAttribute("userName");
        request.removeAttribute("passWord");
        return "login";
    }
}
