package com.wskj.dao;

import com.wskj.dao.handler.OrderMapper;
import com.wskj.model.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by zhuangjy on 2015/7/22.
 */
public class OrderJDBCTemplate {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    /**
     * 创建订单
     * @param orderType
     * @param orderTime
     * @param orderMark
     * @param orderGroup
     */
    public void createOrder(int orderType,Timestamp orderTime,String orderUrl,String orderMark,int orderGroup){
        String sql = "insert into  ods.order(order_type,order_time,order_url,order_mark,order_group) VALUES(?,?,?,?,?)";
        jdbcTemplateObject.update(sql,orderGroup,orderTime,orderUrl,orderMark,orderGroup);
        return;
    }


    public Order getOrder(int orderId){
        String sql = "select * from ods.order where order_id = ?";
        return  jdbcTemplateObject.queryForObject(sql,new Object[]{orderId},new OrderMapper());
    }

    public int getOrderId(int groupId){
        String sql = "select order_id from ods.order where order_group=? order by order_id desc";
        return jdbcTemplateObject.query(sql,new Object[]{groupId},new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt("order_id");
            }
        });
    }
}
