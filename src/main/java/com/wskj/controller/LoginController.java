package com.wskj.controller;

import com.wskj.domain.User;
import com.wskj.service.UserService;
import com.wskj.util.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zhuangjy on 2015/7/7.
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/gotoLogin", method = RequestMethod.GET)
    public String returnLogin() {
        return "login";
    }

    @RequestMapping(value = "/inputLogin", method = RequestMethod.POST)
    public String inputLogin(User user, ModelMap model, HttpSession session,
                             String autoLogin, String checkCode) {
        //自动登录
        User curUser = (User) session.getAttribute("curUser");
        if (curUser != null) {
            user = userService.judgeAccount(curUser);
            if (user != null)
                return "redirect:/gotoIndex";
            else {
                model.addAttribute("error", "登录超时,请重新登录");
                return "login";
            }
        }

        //非自动登录
        String rightVerifyCode = (String) session.getAttribute("randCheckCode");
        if (!checkCode.equalsIgnoreCase(rightVerifyCode)) {
            model.addAttribute("error", "验证码出错!");
            model.addAttribute("userName", user.getUserName());
            model.addAttribute("passWord", user.getPassWord());
            return "login";
        }

        User findUser = userService.judgeAccount(user);
        if (findUser != null) {
            session.setAttribute("curUser", findUser);
            //判断下次是否需要自动登录
            if (autoLogin != "") {
                session.setMaxInactiveInterval(2592000);
            }
            return "redirect:/gotoIndex";
        } else {
            model.addAttribute("error", "用户名、密码错误请重新输入!");
            model.addAttribute("userName", user.getUserName());
            model.addAttribute("passWord", user.getPassWord());
            return "login";
        }
    }

    @RequestMapping(value = "/gotoIndex", method = RequestMethod.GET)
    public String gotoIndex() {
        return "index";
    }

    @RequestMapping(value = "/inputRegister", method = RequestMethod.POST)
    public String Register(@ModelAttribute User user, String passWord2, ModelMap model, HttpServletRequest request) {
        if (!user.getPassWord().equals(passWord2)) {
            model.addAttribute("error", "两次输入密码不一致!");
            return "login";
        }
        User targetUser = userService.getUser(user.getUserName());
        if (targetUser != null) {
            model.addAttribute("error", "邮箱已被注册,请登录!");
            return "login";
        }
        userService.register(user, request);
        model.addAttribute("content", "请登录邮箱点击链接激活该账户!该页面将在5秒后自动关闭...");
        return "transient";
    }


    @RequestMapping(value = "/activateAccount", method = RequestMethod.GET)
    public String activateAccount(String uid, String validkey, ModelMap model, HttpSession session) {
        if (userService.isActable(uid, validkey)) {
            User curUser = userService.getUser(uid);
            session.setAttribute("curUser", curUser);
            return "redirect:/gotoIndex";
        } else {
            model.addAttribute("content", "激活失败,请重新注册...");
            return "transient";
        }
    }

    //获取验证码
    @RequestMapping(value = "/PictureCheckCode", method = RequestMethod.GET)
    public void PictureCheckCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VerifyCodeUtil.VerifyCode(request, response);
    }


    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    @ResponseBody
    public boolean findPassword(String userName, HttpServletRequest request) {
        return userService.findPassword(userName, request);
    }

    @RequestMapping(value = "/gotoResetPassword", method = RequestMethod.GET)
    public String gotoResetPassword(String userName, String validKey, ModelMap model) {
        if (userService.changePassAble(userName, validKey)) {
            model.addAttribute("username", userName);
            return "reset_password";
        } else {
            model.addAttribute("error", "您输入的链接无效");
            return "login";
        }
    }

    @RequestMapping(value = "/resetPassWord", method = RequestMethod.POST)
    public String resetPassWord(String userName, String passWord, ModelMap model) {
        userService.resetPassWord(userName, passWord);
        model.addAttribute("content", "恭喜您修改密码成功,请重新登录,该页面将在5秒后关闭...");
        return "transient";
    }

    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public String loginOut(HttpSession session, HttpServletRequest request) {
        userService.loginOut(session, request);
        return "login";
    }
}
