package com.wskj.service;

import com.wskj.dao.MessageDao;
import com.wskj.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhuangjy on 2015/8/25.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    public int getMessageUnReadNumber(int userId) {
        return messageDao.notifyMessage(userId);
    }

    public List<Message> getRecentMessage(int userId) {
        messageDao.setReaded(userId);
        final int messageNum = 10;
        return messageDao.getRencentMessage(userId, messageNum);
    }

    public void createMessage(int targetId, String message, int type, int groupId, String nickName, int userId) {
        messageDao.createMessage(targetId, message, type, groupId, nickName, userId);
    }

    public void createMessage(int userId, String messageContent, int messageType) {
        messageDao.createMessage(userId, messageContent, messageType);
    }

    public void setMessageResult(int result, int messageId) {
        messageDao.setResult(messageId, result);
    }


    public Message getMessage(int messageId) {
        return messageDao.getMessage(messageId);
    }

}
