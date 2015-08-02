package com.wskj.web;

import com.wskj.dao.GroupDao;
import com.wskj.dao.MessageDao;
import com.wskj.dao.OrderDao;
import com.wskj.dao.UserDao;
import com.wskj.model.Group;
import com.wskj.model.Order;
import com.wskj.model.PersonOrder;
import com.wskj.model.User;
import com.wskj.tools.GetTime;
import com.wskj.tools.SimpleMailSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuangjy on 2015/7/22.
 */
@Controller
public class OrderController {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
    UserDao userDao = (UserDao) ctx.getBean("userDao");
    GroupDao groupDao = (GroupDao) ctx.getBean("groupDao");
    MessageDao messageDao = (MessageDao) ctx.getBean("messageDao");
    OrderDao orderDao = (OrderDao) ctx.getBean("orderDao");


    /**
     * 组长发布消息
     * @param groupId
     * @param orderType
     * @param orderUrl
     * @param orderMark
     * @param session
     * @return
     */
    @RequestMapping(value = "/createOrder" , method = RequestMethod.POST)
    @ResponseBody
    public boolean createOrder(int groupId,int orderType,String orderUrl,String orderMark,String orderEnd,HttpSession session)throws Exception{
        Group group = groupDao.getGroup(groupId);
        User user = (User)session.getAttribute("curUser");
        //获取组长nickName
        String nickName = groupDao.getNickName(user.getId(),groupId);
        //创建订单
        orderDao.createOrder(orderType,new Timestamp(System.currentTimeMillis()),
                orderUrl,orderMark,groupId,new GetTime().convertToTimeStamp(orderEnd));
        //获取订单ID ： 通过获取该组最近的订单
        int orderId = orderDao.getLastOrderId(groupId);

        //获取小组中所有成员id
        List<Integer> memberIds = groupDao.getMemberIds(groupId);
        //获取所有小组成员用户名(邮箱地址)
        List<String> memberEmail = new ArrayList<String>();
        for(Integer t:memberIds)
            memberEmail.add(userDao.getUserName(t));
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


    /**
     * 进入订单详细页
     * @param orderId
     * @param model
     * @return
     */

    @RequestMapping(value = "/startOrder" ,method = RequestMethod.GET)
    public String startOrder(int orderId,ModelMap model){
        Order order = orderDao.getOrder(orderId);
        model.addAttribute("order",order);
        return "order_detail";
    }


    /**
     * 提交自己的订单
     * @param orderId
     * @param orderName
     * @param orderPrice
     * @param orderNumber
     * @param session
     * @return
     */
    @RequestMapping(value = "/submitOrder" , method = RequestMethod.GET)
    @ResponseBody
    public boolean submitOrder(int orderId,String orderName,double orderPrice,int orderNumber,HttpSession session){

        orderName = orderName.trim(); //去除空格
        User user = (User)session.getAttribute("curUser");
        //创建个人表单
        orderDao.createPersonOrder(orderId,user.getId(),orderName,orderNumber,orderPrice);
        //更改总表单的价格
        orderDao.updateOrderPrice(orderId, 0, orderPrice);
        //获取小组信息
        int groupId  = orderDao.getGroupIdFromOrder(orderId);
        Group group = groupDao.getGroup(groupId);
        //获取bossId
        int bossId = group.getGroupBossId();
        //获取昵称
        String nickName = groupDao.getNickName(user.getId(),groupId);
        //告知组长,XX订了一份餐
        String content = nickName +"订了" + orderNumber + "份" + orderName + "总共<font color='red'>" + orderPrice + "</font>元";
        messageDao.createMessage(bossId,content,0);
        return true;
    }


    @RequestMapping(value = "/getOrder" , method = RequestMethod.POST)
    @ResponseBody
    public List<Order> getOrder(String startTime,String endTime,String url,int groupId,int start,int limit) throws ParseException {
        GetTime gt = new GetTime();
        return orderDao.searchOrder(gt.convertToTimeStamp(startTime),
                gt.convertToTimeStamp(endTime),groupId,url);
    }



    @RequestMapping(value = "/getDetailInfo" , method = RequestMethod.GET)
    @ResponseBody
    public List<PersonOrder> getDetailInfo(int orderId,int groupId){
        List<PersonOrder> personOrders =  orderDao.getDetailInfo(orderId);
        for(PersonOrder t:personOrders) //设置所有昵称
            t.setNickName(groupDao.getNickName(t.getUserId(), groupId));
        return personOrders;
    }

    @RequestMapping(value = "/countResult", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Integer> countResult(int orderId){
        Map<String,Integer> map = new HashMap<String, Integer>();
        List<PersonOrder> personOrders = orderDao.getDetailInfo(orderId);
        for(PersonOrder t:personOrders){
            String name = t.getOrderName();
            if(map.get(name) != null)
                map.put(name,map.get(name) + 1);
            else
                map.put(name,1);
        }
        return map;
    }

    @RequestMapping(value = "/currentOrder" ,method = RequestMethod.GET)
    @ResponseBody
    public List<PersonOrder> currentOrder(int groupId){
        List<PersonOrder> personOrders = orderDao.getLastOrder(groupId);
        if(personOrders!=null) //设置别名
            for(PersonOrder t:personOrders)//设置所有昵称
                t.setNickName(groupDao.getNickName(t.getUserId(),groupId));
        return personOrders;
    }

    @RequestMapping(value = "/currentOrderCount", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> currentOrderCount(int groupId){
        int orderId = orderDao.getLastOrderId(groupId);
        Map<String,Object> map = new HashMap<String, Object>();
        List<PersonOrder> personOrders = orderDao.getDetailInfo(orderId);
        for(PersonOrder t:personOrders){
            String name = t.getOrderName();
            if(map.get(name) != null)
                map.put(name,(Integer)map.get(name) + 1);
            else
                map.put(name,1);
        }
        //获取订单的总价
        double price = orderDao.getOrderPrice(orderId);
        map.put("price",price);
        return map;
    }
}
