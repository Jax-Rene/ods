package com.wskj.dao;

import com.wskj.dao.handler.MessageMapper;
import com.wskj.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zhuangjy on 2015/7/17.
 */
@Component
public class MessageDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createMessage(int userId,String messageContent,int messageType){
        String sql = "INSERT INTO ods.message(user_id,message_type,message_content,message_time) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql,userId,messageType,messageContent,new Timestamp(System.currentTimeMillis()));
        System.out.println("创建通知消息成功!");
        return;
    }

    public void createMessage(int userId,String message_Content,int messageType,int messageGroupId,String messageFrom,int messageFromId){
        String sql = "INSERT INTO ods.message(user_id,message_type,message_content,message_time,message_group_id,message_from,message_from_id) VALUES(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,userId,messageType,message_Content,new Timestamp(System.currentTimeMillis()),messageGroupId,messageFrom,messageFromId);
        System.out.println("创建请求、邀请成功!");
        return;
    }

    //消息通知，查看有几条未读的消息,返回0代表物最新消息
    public int notifyMessage(int userId){
        String sql = "select count(*) as total from ods.message where message_result=0 and user_id=?";
        return jdbcTemplate.query(sql,new Object[]{userId},new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    rs.next();
                    return rs.getInt("total");
            }
        });
    }

    /**
     * 将所有未读消息设置为已读
     * 0 - 未读 （系统提醒)
     * 1 - 接受
     * -1 - 拒绝
     * 2 - 已读
     * @param userId
     */
    public void setReaded(int userId){
        String sql = "update ods.message set message_result=2 where message_result=0 and user_id = ?";
        jdbcTemplate.update(sql,userId);
        return;
    }

    //获取最近的n条消息
    public List<Message> getRencentMessage(int userId,int num){
        String sql = "select * from ods.message  where user_id=?  ORDER BY message_time desc limit ? offset 0";
        List<Message> messages = jdbcTemplate.query(sql,new Object[]{userId,num},new MessageMapper());
        return messages;
    }


    //设置请求、邀请结果
    public void setResult(int messageId,int result){
        String sql = "update ods.message set message_result=? where message_id =?";
        System.out.println(sql);
        System.out.println(messageId);
        jdbcTemplate.update(sql,result,messageId);
    }


    //获取消息bean
    public Message getMessage(int messageId){
        String sql = "select * from ods.message where message_id = ? ";
        return jdbcTemplate.queryForObject(sql,new Object[]{messageId},new MessageMapper());
    }
}
