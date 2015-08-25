package com.wskj.controller;

import com.wskj.domain.Group;
import com.wskj.domain.Order;
import com.wskj.domain.PersonOrder;
import com.wskj.domain.User;
import com.wskj.service.GroupService;
import com.wskj.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuangjy on 2015/7/22.
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GroupService groupService;


    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseBody
    public boolean createOrder(int groupId, int orderType, String orderUrl, String orderMark, String orderEnd,
                               HttpServletRequest request, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("curUser");
        return orderService.createOrder(groupId, orderType, orderUrl, orderMark, orderEnd, request, user.getId());
    }


    /**
     * 进入订单详细页
     *
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "/startOrder", method = RequestMethod.GET)
    public String startOrder(int orderId, ModelMap model) {
        Order order = orderService.getOrder(orderId);
        int groupId = order.getOrderGroup();
        Group group = groupService.getGroupById(groupId);
        model.addAttribute("order", order);
        model.addAttribute("group",group);
        return "order_detail";
    }


    /**
     * 提交自己的订单
     *
     * @param orderId
     * @param orderName
     * @param orderPrice
     * @param orderNumber
     * @param session
     * @return
     */
    @RequestMapping(value = "/submitOrder", method = RequestMethod.GET)
    @ResponseBody
    public boolean submitOrder(int orderId, String orderName, double orderPrice, int orderNumber, HttpSession session) {
        User user = (User) session.getAttribute("curUser");
        return orderService.submitOrder(orderId, orderName, orderPrice, orderNumber, user.getId());
    }


    /**
     * 获取订单详情
     *
     * @param startTime
     * @param endTime
     * @param url
     * @param groupId
     * @param start
     * @param limit
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    @ResponseBody
    public List<Order> getOrder(String startTime, String endTime, String url, int groupId, int start, int limit) throws ParseException {
        return orderService.getOrder(startTime, endTime, url, groupId, start, limit);
    }


    @RequestMapping(value = "/getDetailInfo", method = RequestMethod.GET)
    @ResponseBody
    public List<PersonOrder> getDetailInfo(int orderId, int groupId) {
        return orderService.getDetailInfo(orderId, groupId);
    }

    @RequestMapping(value = "/countResult", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> countResult(int orderId) {
        return orderService.countResult(orderId);
    }

    /**
     * 小组最近的订单
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/currentOrder", method = RequestMethod.GET)
    @ResponseBody
    public List<PersonOrder> currentOrder(int groupId) {
        return orderService.currentOrder(groupId);
    }


    @RequestMapping(value = "/currentOrderCount", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> currentOrderCount(int groupId) {
        int orderId = orderService.getLastOrderId(groupId);
        if (orderId != -1)
            return orderService.countResult(orderId);
        else
            return new HashMap<String, Object>();
    }

    @RequestMapping(value = "/deletePersonOrder", method = RequestMethod.POST)
    @ResponseBody
    public boolean deletePersonOrder(int id, int orderId, int targetUserId, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        return orderService.deletePersonOrder(id, orderId, targetUserId, curUser.getId());
    }

    @RequestMapping(value = "/editPersonOrder", method = RequestMethod.POST)
    @ResponseBody
    public boolean editPersonOrder(PersonOrder personOrder, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        return orderService.editPersonOrder(personOrder, curUser.getId());
    }

    @RequestMapping(value = "/insertPersonOrder", method = RequestMethod.POST)
    @ResponseBody
    public boolean insertPersonOrder(PersonOrder personOrder, int groupId, HttpSession session) {
        User user = (User) session.getAttribute("curUser");
        return orderService.insertPersonOrder(personOrder, groupId, user.getId());
    }


}
