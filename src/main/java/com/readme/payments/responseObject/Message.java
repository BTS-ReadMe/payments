package com.readme.payments.responseObject;

public class Message<T> {

    private T data;

    public void setData(T data) {
        this.data = data;
    }
}
