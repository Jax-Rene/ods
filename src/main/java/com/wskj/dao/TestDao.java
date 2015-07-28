package com.wskj.dao;

import com.wskj.model.Order;
import com.wskj.model.PersonOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuangjy on 2015/7/27.
 */
public class TestDao {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        OrderDao orderDao = (OrderDao) ctx.getBean("orderDao");
        Date date = new Date("2015-07-23");
    }
}
