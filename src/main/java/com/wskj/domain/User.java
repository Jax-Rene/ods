package com.wskj.domain;

/**
 * Created by zhuangjy on 2015/7/7.
 */
public class User {
    private Integer id;
    private String userName;
    private String passWord;
    private String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User(Integer id, String userName, String passWord, String location) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.location = location;
    }

    public User() {
    }
}
