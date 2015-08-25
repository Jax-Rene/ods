package com.wskj.dao.handler;

import com.wskj.domain.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhuangjy on 2015/7/17.
 */
public class MessageMapper implements RowMapper<Message> {
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setMessageId(rs.getInt("message_id"));
        message.setUserId(rs.getInt("user_id"));
        message.setMessageType(rs.getInt("message_type"));
        message.setMessageContent(rs.getString("message_content"));
        message.setMessageTime(rs.getTimestamp("message_time"));
        message.setMessageResult(rs.getInt("message_result"));
        message.setMessageFrom(rs.getString("message_from"));
        message.setMessageGroupId(rs.getInt("message_group_id"));
        message.setMessageFromId(rs.getInt("message_from_id"));
        return message;
    }
}
