package com.wskj.web;

import com.wskj.dao.MessageDao;
import com.wskj.model.Message;
import com.wskj.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zhuangjy on 2015/7/17.
 */
@Controller
public class MessageController {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
    MessageDao messageDao = (MessageDao) ctx.getBean("messageDao");

    @RequestMapping(value="/notifyMessage")
    @ResponseBody
    public int notifyMessage(HttpSession session){
        User curUser = (User)session.getAttribute("curUser");
        System.out.println("userId : " + curUser.getId());
        int messageNum = messageDao.notifyMessage(curUser.getId());
        return messageNum;
    }

    @RequestMapping(value = "/getRencentMessage")
    @ResponseBody
    public List<Message> getRencentMessage(HttpSession session){
        User curUser = (User)session.getAttribute("curUser");
        //将所有的未读消息标志为已读
        messageDao.setReaded(curUser.getId());
        //获取最近的十条消息
        List<Message> lastMessage = messageDao.getRencentMessage(curUser.getId(),10);
        System.out.println(lastMessage.get(0).getMessageTime());
        return lastMessage;
    }
}
