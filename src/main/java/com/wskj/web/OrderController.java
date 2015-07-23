package com.wskj.web;

import com.sun.javafx.sg.prism.NGShape;
import com.wskj.dao.GroupJDBCTemplate;
import com.wskj.dao.MessageJDBCTemplate;
import com.wskj.dao.OrderJDBCTemplate;
import com.wskj.dao.UserJDBCTemplate;
import com.wskj.model.Group;
import com.wskj.model.Order;
import com.wskj.model.User;
import com.wskj.tools.SimpleMailSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangjy on 2015/7/22.
 */
@Controller
public class OrderController {

    ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
    UserJDBCTemplate userJDBCTemplate = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
    GroupJDBCTemplate groupJDBCTemplate = (GroupJDBCTemplate) context.getBean("groupJDBCTemplate");
    MessageJDBCTemplate messageJDBCTemplate = (MessageJDBCTemplate) context.getBean("messageJDBCTemplate");
    OrderJDBCTemplate orderJDBCTemplate = (OrderJDBCTemplate) context.getBean("orderJDBCTemplate");

    @RequestMapping(value = "/createOrder" , method = RequestMethod.POST)
    @ResponseBody
    public boolean createOrder(int groupId,int orderType,String orderUrl,String orderMark,HttpSession session){
        Group group = groupJDBCTemplate.getGroup(groupId);
        User user = (User)session.getAttribute("curUser");
        //创建订单
        orderJDBCTemplate.createOrder(orderType,new Timestamp(System.currentTimeMillis()),orderUrl,orderMark,groupId);
        //获取订单ID ： 通过获取该组最近的订单
        int orderId = orderJDBCTemplate.getOrderId(groupId);

        //获取小组中所有成员id
        List<Integer> memberIds = groupJDBCTemplate.getMemberIds(groupId);
        //获取所有小组成员用户名(邮箱地址)
        List<String> memberEmail = new ArrayList<String>();
        for(Integer t:memberIds)
            memberEmail.add(userJDBCTemplate.getUserName(t));
        //订餐信息
        String subject = "来自" + group.getGroupName() + "的订餐订单";
        String content = "<h1>小组:" + group.getGroupName() + "</h1>\n" +
                "<h2>组长:" + user.getUserName() + "</h2>\n" +
                "<hr/>\n" +
                "<ul>\n" +
                "<li>\n" +
                "<h4><a href=startOrder?orderId='" + orderId +"'>点击进入今天的订餐地址</a></h4>\n" +
                "</li>\n" +
                "<li>\n" +
                "<h4>来自组长的备注:</h4>\n" +
                "<h5>" + orderMark + "</h5>\n" +
                "</li>";
        //发送给组内所有成员
        SimpleMailSender simpleMailSender = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
        try {
            simpleMailSender.send(memberEmail,subject,content);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/startOrder" ,method = RequestMethod.GET)
    public String startOrder(int orderId,ModelMap model){
        Order order = orderJDBCTemplate.getOrder(orderId);
        model.addAttribute("order",order);
        return "order_detail";
    }
}
