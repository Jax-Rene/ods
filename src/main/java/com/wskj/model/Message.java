package com.wskj.model;

import com.wskj.tools.CustomDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.sql.Timestamp;

/**
 * Created by zhuangjy on 2015/7/17.
 */
public class Message {
    private int messageId;
    private int userId;
    private int messageType;
    private String messageContent;
    private Timestamp messageTime;
    private int messageResult;
    private int messageGroupId;
    private String messageFrom;
    private int messageFromId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Timestamp getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageResult() {
        return messageResult;
    }

    public void setMessageResult(int messageResult) {
        this.messageResult = messageResult;
    }

    public int getMessageGroupId() {
        return messageGroupId;
    }

    public void setMessageGroupId(int messageGroupId) {
        this.messageGroupId = messageGroupId;
    }


    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public int getMessageFromId() {
        return messageFromId;
    }

    public void setMessageFromId(int messageFromId) {
        this.messageFromId = messageFromId;
    }

    public Message(){}
}
