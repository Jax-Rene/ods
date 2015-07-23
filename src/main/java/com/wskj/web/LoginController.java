package com.wskj.web;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wskj.dao.GroupJDBCTemplate;
import com.wskj.dao.UserJDBCTemplate;
import com.wskj.model.Group;
import com.wskj.model.User;
import com.wskj.tools.GetTime;
import com.wskj.tools.MD5Util;
import com.wskj.tools.SimpleMailSender;
import com.wskj.tools.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

/**
 * Created by zhuangjy on 2015/7/7.
 */
@Controller
@SessionAttributes("curUser")
public class LoginController {
    private ApplicationContext context =
            new ClassPathXmlApplicationContext("spring-config.xml");
    UserJDBCTemplate userJDBCTemplate =
            (UserJDBCTemplate)context.getBean("userJDBCTemplate");
    GroupJDBCTemplate groupJDBCTemplate = (GroupJDBCTemplate) context.getBean("groupJDBCTemplate");

    @RequestMapping(value ="/gotoLogin",method = RequestMethod.GET)
    public String returnLogin(){
        return "login";
    }

    @RequestMapping(value = "/inputLogin",method = RequestMethod.POST)
    public String isLogin(@ModelAttribute User user,ModelMap model, HttpServletRequest request,
                          HttpServletResponse response,String checkCode){
        HttpSession session = request.getSession();
        String rightVerifyCode  =(String) session.getAttribute("randCheckCode");

        //检查是否存在cookie,存在cookie即不用判断验证码
        Cookie[] cookies = request.getCookies();
        Boolean existCookie = false;
        for (Cookie t:cookies){
            if(t.getName().equals("username") || t.getName().equals("password")){
                existCookie = true;
                break;
            }
        }

        if(!existCookie && !checkCode.equalsIgnoreCase(rightVerifyCode)) {
            model.addAttribute("passError", "验证码出错!");
            return "login";
        }
        user = userJDBCTemplate.judgeAccount(user.getUserName(),user.getPassWord());
        if(user!=null){
            model.addAttribute("curUser",user); //加入seession
            System.out.println("登录成功");
            return "index"; //登陆成功返回首页
        }

        //密码错误清除cookie 返回首页
        if(existCookie){
        for (Cookie t:cookies){
            if(t.getName().equals("username") || t.getName().equals("password")){
                t.setMaxAge(0); //删除cookie
                response.addCookie(t); //要刷新response不然不起作用
            }
        }}
        model.addAttribute("passError", "用户名、密码错误请重新输入!");
        return "login";
    }

    @RequestMapping(value = "/gotoRegister",method = RequestMethod.GET)
    public String getRegister(){
        return "register"; //返回注册页面
    }

    @RequestMapping(value = "/inputRegister",method = RequestMethod.POST)
    public String Register(@ModelAttribute User user,ModelMap model){
        try{
            User t = userJDBCTemplate.getUser(user.getUserName());
            //如果搜索到结果不为空则出错
            model.addAttribute("error", "邮箱已被注册,请登录!");
            return "register";
        }catch (EmptyResultDataAccessException empty){ //为空则表示正常
                SimpleMailSender sms = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
                String recipient = user.getUserName();
                java.sql.Timestamp ts = new GetTime().getOutTime();
                Random random = new Random();
                String key = user.getUserName() + "|" + ts + "|" + random.nextInt();
                String signature = MD5Util.MD5(key);
                userJDBCTemplate.insertAct(user.getUserName(), user.getPassWord(), signature, user.getLocation(), ts);
                String url = "localhost:8080/activateAccount" + "?uid=" + user.getUserName() + "&amp;validkey=" + signature;
                try {
                    sms.send(recipient, "欢迎注册ods系统", url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                model.addAttribute("content","请登录邮箱点击链接激活该账户!该页面将在5秒后自动关闭!");
                return "transient";
            }
    }

    @RequestMapping(value = "/activateAccount",method = RequestMethod.GET)
    public String activateAccount(String uid,String validkey,ModelMap model){
        boolean judge = userJDBCTemplate.isAct(uid,validkey);
        if(judge){ //激活成功，并且获取加入session
            //获取用户
            User curUser = userJDBCTemplate.getUser(uid);
            model.addAttribute("curUser", userJDBCTemplate.getUser(uid));
            return "index";
        }else {
            model.addAttribute("content", "激活失败,请重新注册!");
            return "transient";
        }
    }

    //获取验证码
    @RequestMapping(value = "/PictureCheckCode",method = RequestMethod.GET)
    public void PictureCheckCode( HttpServletRequest request,HttpServletResponse response) throws  Exception{
        VerifyCodeUtil verifycode = new VerifyCodeUtil();
        verifycode.VerifyCode(request,response);
    }


    /**
     * 用于找回密码
     */

        @RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
        public ModelAndView returnFindPassword() {
            return new ModelAndView("findpassword");//返回找回密码页面
        }

        @RequestMapping(value = "/findPassword", method = RequestMethod.GET)
        public String findPassword(String userName, ModelMap model) throws MessagingException {
            System.out.println("用户名为:" + userName);
            //判断用户是否存在
            User user = userJDBCTemplate.getUser(userName);
            if(user!=null) {
                SimpleMailSender sms = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
                String recipient = user.getUserName();
                java.sql.Timestamp ts = new GetTime().getOutTime();
                Random random = new Random();
                String key = user.getUserName() + "|" + ts + "|" + random.nextInt();
                String signature = MD5Util.MD5(key);
                //插入数据库表
                userJDBCTemplate.insertInfor(userName,ts,signature);
                String url = "http://localhost:8080/gotoResetPassword" + "?userName=" + userName + "&amp;validKey=" + signature;
                sms.send(recipient, "ods找回密码", url);
                model.addAttribute("content","请登录邮箱点击邮箱链接认证身份,本页面将在5秒后转向首页!");
                return "transient";
            }else {
                model.addAttribute("error", "不存在的账号,请注册!");
                return "findpassword";
            }
        }

        //关于找回密码的页面   ：输入两次新密码
        @RequestMapping(value = "/gotoResetPassword", method = RequestMethod.GET)
        public String gotoResetPassword(String userName,String validKey, ModelMap model) {
            //判断是否有权限修改密码
            if(userJDBCTemplate.isChangePass(userName,validKey)) {
                model.addAttribute("username",userName);
                return "resetpassword";
            }else {
                model.addAttribute("passError", "您输入的链接有误");
                return "login";
            }
        }

        //重置密码
        @RequestMapping(value = "/resetPassWord" , method = RequestMethod.POST)
        public  String resetPassWord(String userName,String passWord,ModelMap model){
            userJDBCTemplate.resetPassWord(userName,passWord);
            model.addAttribute("content","恭喜您修改密码成功,请重新登录,该页面将在5秒后关闭!");
            return "transient";
        }

}
