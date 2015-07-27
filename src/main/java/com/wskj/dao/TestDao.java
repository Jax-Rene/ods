package com.wskj.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhuangjy on 2015/7/27.
 */
public class TestDao {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        UserDao userDao = (UserDao) ctx.getBean("userDao");
        userDao.getUser(9);
    }
}
