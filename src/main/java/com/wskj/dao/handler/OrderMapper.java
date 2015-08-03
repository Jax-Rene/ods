package com.wskj.dao.handler;

import com.wskj.model.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhuangjy on 2015/7/22.
 */
public class OrderMapper implements RowMapper<Order> {
    public Order mapRow(ResultSet rs,int rowNum) throws SQLException{
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderType(rs.getInt("order_type"));
        order.setOrderTime(rs.getTimestamp("order_time"));
        order.setOrderMark(rs.getString("order_mark"));
        order.setOrderGroup(rs.getInt("order_group"));
        order.setOrderPrice(rs.getDouble("order_price"));
        order.setOrderUrl(rs.getString("order_url"));
        order.setOrderEnd(rs.getTimestamp("order_end"));
        return order;
    }
}
