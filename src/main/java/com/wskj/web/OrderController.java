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
        //获取组长nickName
        String nickName = groupJDBCTemplate.getNickName(user.getId(),groupId);
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
        String typeName = null;
        switch (orderType){
            case 0:
                typeName = "早餐";
                break;
            case 1:
                typeName = "午餐";
                break;
            case 3:
                typeName = "晚餐";
                break;
            default:
                typeName = "其他";
        }
        //订餐信息
        String subject = "来自" + group.getGroupName() + "的订餐订单";
        String content = "<h2>小组:" + group.getGroupName() + "</h2><h2>组长:" + nickName + "</h2><h2>订餐类型:" + typeName +"</h2>" +
                "<hr/>" +
                "<ul>" +
                "<li>" +
                "<a href='http://localhost:8080/startOrder?orderId="+orderId +"'>点击进入今天的订餐地址</a>" +
                "</li>" +
                "<li>" +
                "<h4>来自组长的备注:</h4>" +
                "<h5>" + orderMark + "</h5>" +
                "</li>";
        //发送给组内所有成员 localhost:8080/startOrder?orderId="+orderId +"
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


    @RequestMapping(value = "/submitOrder" , method = RequestMethod.GET)
    @ResponseBody
    public boolean submitOrder(int orderId,String orderName,double orderPrice,int orderNumber,HttpSession session){
        System.out.println("进来了!");
        User user = (User)session.getAttribute("curUser");
        //创建个人表单
        orderJDBCTemplate.createPersonOrder(orderId,user.getId(),orderName,orderNumber,orderPrice);
        //更改总表单的价格
        orderJDBCTemplate.updateOrderPrice(orderId, 0, orderPrice);
        //获取小组信息
        int groupId  = orderJDBCTemplate.getGroupIdFromOrder(orderId);
        Group group = groupJDBCTemplate.getGroup(groupId);
        //获取bossId
        int bossId = group.getGroupBossId();
        //获取昵称
        String nickName = groupJDBCTemplate.getNickName(user.getId(),groupId);
        //告知组长,XX订了一份餐
        String content = nickName +"订了" + orderNumber + "份" + orderName + "总共<font color='red'>" + orderPrice + "</font>元";
        messageJDBCTemplate.createMessage(bossId,content,0);
        return true;
    }


}
