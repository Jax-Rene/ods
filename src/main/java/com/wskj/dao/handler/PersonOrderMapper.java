package com.wskj.dao.handler;

import com.wskj.model.Message;
import com.wskj.model.PersonOrder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhuangjy on 2015/7/23.
 */
public class PersonOrderMapper implements RowMapper<PersonOrder> {
    public PersonOrder mapRow(ResultSet rs,int rowNum)throws SQLException {
        PersonOrder personOrder = new PersonOrder();
        personOrder.setId(rs.getInt("id"));
        personOrder.setOrderId(rs.getInt("order_id"));
        personOrder.setUserId(rs.getInt("user_id"));
        personOrder.setOrderName(rs.getString("order_name"));
        personOrder.setOrderNumber(rs.getInt("order_number"));
        personOrder.setOrderPrice(rs.getDouble("order_price"));
        return personOrder;
    }
}
