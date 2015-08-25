package com.wskj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.dao.GroupDao;
import com.wskj.dao.MessageDao;
import com.wskj.dao.OrderDao;
import com.wskj.dao.UserDao;
import com.wskj.domain.Group;
import com.wskj.domain.Order;
import com.wskj.domain.PersonOrder;
import com.wskj.domain.User;
import com.wskj.util.GetTime;
import com.wskj.util.SimpleMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuangjy on 2015/8/25.
 */
@Service
public class OrderService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private OrderDao orderDao;

    public boolean createOrder(int groupId, int orderType, String orderUrl, String orderMark, String orderEnd,
                               HttpServletRequest request, int userId) {
        Group group = groupDao.getGroup(groupId);
        String nickName = groupDao.getNickName(userId, groupId);
        try {
            orderDao.createOrder(orderType, new Timestamp(System.currentTimeMillis()),
                    orderUrl, orderMark, groupId, GetTime.convertToTimeStamp(orderEnd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取订单ID ： 通过获取该组最近的订单
        int orderId = orderDao.getLastOrderId(groupId);
        //获取小组中所有成员id
        List<Integer> memberIds = groupDao.getMemberIds(groupId);
        //获取所有小组成员用户名(邮箱地址)
        List<String> memberEmail = Lists.newArrayList();
        for (Integer t : memberIds)
            memberEmail.add(userDao.getUserName(t));
        String typeName = null;
        switch (orderType) {
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
        //发送订餐信息
        String absoluteContextPath = (String) request.getAttribute("absoluteContextPath");
        String url = absoluteContextPath + "/startOrder?orderId=" + orderId;
        String subject = "来自" + group.getGroupName() + "的订餐订单";
        String content = "<h2>小组:" + group.getGroupName() + "</h2><h2>组长:" + nickName + "</h2><h2>订餐类型:" + typeName + "</h2>" +
                "<hr/>" +
                "<ul>" +
                "<li>" +
                "<a href='" + url + "'>点击进入今天的订餐地址</a>" +
                "</li>" +
                "<li>" +
                "<h4>来自组长的备注:</h4>" +
                "<h5>" + orderMark + "</h5>" +
                "</li>";
        SimpleMailSender simpleMailSender = new SimpleMailSender("249602015@qq.com", "joy-zhuang");
        try {
            simpleMailSender.send(memberEmail, subject, content);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public Order getOrder(int orderId) {
        return orderDao.getOrder(orderId);
    }

    public boolean submitOrder(int orderId, String orderName, double orderPrice, int orderNumber, int userId) {
        orderName = orderName.trim();
        //创建个人表单
        orderDao.createPersonOrder(orderId, userId, orderName, orderNumber, orderPrice);
        //更改总表单的价格
        orderDao.updateOrderPrice(orderId, 0, orderPrice);
        //获取小组信息
        int groupId = orderDao.getGroupIdFromOrder(orderId);
        Group group = groupDao.getGroup(groupId);
        //获取bossId
        int bossId = group.getGroupBossId();
        //获取昵称
        String nickName = groupDao.getNickName(userId, groupId);
        //告知组长,XX订了一份餐
        String content = nickName + "订了" + orderNumber + "份" + orderName + "总共<font color='red'>" + orderPrice + "</font>元";
        messageDao.createMessage(bossId, content, 0);
        return true;
    }


    public List<Order> getOrder(String startTime, String endTime, String url, int groupId, int start, int limit) throws ParseException {
        return orderDao.searchOrder(GetTime.convertToTimeStamp(startTime),
                GetTime.convertToTimeStamp(endTime), groupId, url);
    }

    public List<PersonOrder> getDetailInfo(int orderId, int groupId) {
        List<PersonOrder> personOrders = orderDao.getDetailInfo(orderId);
        //设置所有昵称
        for (PersonOrder t : personOrders)
            t.setNickName(groupDao.getNickName(t.getUserId(), groupId));
        return personOrders;
    }


    /**
     * 统计订单信息
     *
     * @param orderId
     * @return
     */
    public Map<String, Object> countResult(int orderId) {
        Map<String, Object> map = Maps.newHashMap();
        List<PersonOrder> personOrders = orderDao.getDetailInfo(orderId);
        List<String> orderNames = Lists.newArrayList();
        /**
         获取所有套餐的完整名字
         例如 105 北京烤鸭
         输入105 应该获取 105 北京烤鸭
         */
        for (int i = 0; i < personOrders.size(); i++) {
            String targetName = personOrders.get(i).getOrderName();
            boolean flag = true;
            for (int j = 0; j < orderNames.size(); j++) {
                String curName = orderNames.get(j);
                if ((targetName.indexOf(curName) != -1) || (curName.indexOf(targetName) != -1)) {
                    flag = false;
                    if (targetName.length() > curName.length()) {
                        orderNames.remove(j);
                        orderNames.add(targetName);
                        break;
                    }
                }
            }
            if (flag)
                orderNames.add(targetName);
        }

        for (PersonOrder t : personOrders) {
            String name = t.getOrderName();
            for (String n : orderNames) {
                if (n.indexOf(name) != -1) {

                    map.put(n, (Integer) map.get(n) + t.getOrderNumber());
                } else
                    map.put(n, 1);
            }
        }
        return map;
    }

    public List<PersonOrder> currentOrder(int groupId) {
        List<PersonOrder> personOrders = orderDao.getLastOrder(groupId);
        //设置别名
        if (personOrders != null)
            for (PersonOrder t : personOrders)
                t.setNickName(groupDao.getNickName(t.getUserId(), groupId));
        return personOrders;
    }

    public int getLastOrderId(int groupId) {
        return orderDao.getLastOrderId(groupId);
    }

    public boolean deletePersonOrder(int id, int orderId, int targetUserId, int curUserId) {
        Order order = orderDao.getOrder(orderId);
        int groupId = order.getOrderGroup();
        int bossId = groupDao.getBossId(groupId);
        if (curUserId != targetUserId && bossId != curUserId) {
            return false;
        } else {
            orderDao.deletePersonOrder(id, orderId);
            return true;
        }
    }

    public boolean editPersonOrder(PersonOrder personOrder, int userId) {
        Order order = orderDao.getOrder(personOrder.getOrderId());
        int bossId = groupDao.getBossId(order.getOrderGroup());
        if (userId != personOrder.getUserId() && bossId != userId) {
            return false;
        } else {
            //删除订单名存在的空格
            personOrder.setOrderName(personOrder.getOrderName().trim());
            orderDao.editPersonOrder(personOrder);
            return true;
        }
    }

    public boolean insertPersonOrder(PersonOrder personOrder, int groupId, int userId) {
        //最新的订单
        if (personOrder.getOrderId() == 0) {
            personOrder.setOrderId(orderDao.getLastOrderId(groupId));
        }
        Order order = orderDao.getOrder(personOrder.getOrderId());
        int bossId = groupDao.getBossId(order.getOrderGroup());
        //只能创建自己的订单
        if (userId != personOrder.getUserId() && bossId != userId) {
            return false;
        } else {
            //删除订单名存在的空格
            personOrder.setOrderName(personOrder.getOrderName().trim());
            //获取目标用户的userId
            orderDao.insertPersonOrder(personOrder);
            return true;
        }
    }


}
