package com.esri.arcgisruntime.container.monitoring.http;

import com.google.gson.annotations.SerializedName;


public class ResponseJson<T> {
    @SerializedName("data")
    private T data;
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
