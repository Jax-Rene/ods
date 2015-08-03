package com.wskj.filter;

import com.wskj.dao.UserDao;
import com.wskj.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zhuangjy on 2015/7/27.
 */
public class LoginFilter extends OncePerRequestFilter{

    private ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
    UserDao userDao = (UserDao) ctx.getBean("userDao");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI(); // 获取URI
        //设置不过滤的URI
        String[] unFilter = {"/","/gotoLogin","redirect_index.jsp","/inputLogin","/gotoRegister",
                "/inputRegister","/activateAccount","/PictureCheckCode","/forgetPassword",
                "/findPassword","/gotoResetPassword","/resetPassWord","/js/","/img/","/css/"
        };
        boolean flag = true; //判断是否过滤
        for(String s:unFilter){
            if(uri.indexOf(s) != -1) {
                flag = false;
                break;
            }
        }

        if(flag) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("curUser");
            if (user == null) {
                //判断是否存在cookie
                Cookie[] cookies = request.getCookies();
                String userName = null;
                String passWord = null;

                if(cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("username"))
                            userName = cookie.getValue();
                        else if (cookie.getName().equals("password")) {
                            passWord = cookie.getValue();
                            break;
                        }
                    }
                }
                if (userName != null && passWord != null)
                    user = userDao.judgeAccount(userName, passWord);

                if(user == null)
                    response.sendRedirect("gotoLogin");
                else
                    session.setAttribute("curUser",user);
            }
        }
            filterChain.doFilter(request, response);
    }
}
