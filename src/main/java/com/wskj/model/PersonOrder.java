package com.wskj.model;

/**
 * Created by zhuangjy on 2015/7/23.
 */
public class PersonOrder {
    private int id;
    private int orderId;
    private int userId;
    private String nickName;
    private String orderName;
    private int orderNumber;
    private double orderPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PersonOrder() {
    }

    public PersonOrder(int id, int orderId,int userId, String orderName, int orderNumber, double orderPrice) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.orderName = orderName;
        this.orderNumber = orderNumber;
        this.orderPrice = orderPrice;
    }

    public PersonOrder(int userId, double orderPrice, int orderNumber, String orderName, int orderId) {
        this.userId = userId;
        this.orderPrice = orderPrice;
        this.orderNumber = orderNumber;
        this.orderName = orderName;
        this.orderId = orderId;
    }
}
