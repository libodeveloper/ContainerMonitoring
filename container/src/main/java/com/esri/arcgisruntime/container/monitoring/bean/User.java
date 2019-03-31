package com.esri.arcgisruntime.container.monitoring.bean;

import java.io.Serializable;

public class User implements Serializable{

//    {
//        "keyId": "6df5fe45dca442b090a80b83445d1f78",
//            "key": "d6325521c0bc49399f471bd85f2d2d15",
//            "account": "admin"
//    }

    private int userId;

    private String keyId;
    private String key="";
    private String account;
    private String password;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
