package com.wskj.model;

import java.sql.Timestamp;

/**
 * Created by zhuangjy on 2015/7/22.
 */
public class Order {
    private int orderId;
    private int orderType;
    private Timestamp orderTime;
    private String orderMark;
    private int orderGroup;
    private double orderPrice;
    private String orderUrl;
    private Timestamp orderEnd;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderMark() {
        return orderMark;
    }

    public void setOrderMark(String orderMark) {
        this.orderMark = orderMark;
    }

    public int getOrderGroup() {
        return orderGroup;
    }

    public void setOrderGroup(int orderGroup) {
        this.orderGroup = orderGroup;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public Timestamp getOrderEnd() {
        return orderEnd;
    }

    public void setOrderEnd(Timestamp orderEnd) {
        this.orderEnd = orderEnd;
    }

    public Order() {
    }

    public Order(int orderId, int orderType, Timestamp orderTime, String orderMark, int orderGroup, double orderPrice, String orderUrl, Timestamp orderEnd) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.orderTime = orderTime;
        this.orderMark = orderMark;
        this.orderGroup = orderGroup;
        this.orderPrice = orderPrice;
        this.orderUrl = orderUrl;
        this.orderEnd = orderEnd;
    }
}
