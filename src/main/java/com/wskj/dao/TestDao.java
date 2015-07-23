package com.wskj.dao;

import com.wskj.model.Group;
import com.wskj.model.User;
import com.wskj.tools.GetTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangjy on 2015/7/8.
 */

/*

测试数据库
 */
public class TestDao {
    //单元测试
    public static void main(String []args){
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring-config.xml");

        MessageJDBCTemplate jdbcTemplate =
                (MessageJDBCTemplate)context.getBean("messageJDBCTemplate");

        UserJDBCTemplate userJDBCTemplate =
                (UserJDBCTemplate)context.getBean("userJDBCTemplate");

        GroupJDBCTemplate groupJDBCTemplate = (GroupJDBCTemplate)context.getBean("groupJDBCTemplate");


        List<Group> groupIds = groupJDBCTemplate.getUnderGroup(1);

    }
}
