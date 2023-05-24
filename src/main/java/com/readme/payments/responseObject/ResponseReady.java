package com.readme.payments.responseObject;

import lombok.Getter;

@Getter
public class ResponseReady {

    private String tid;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setNext_redirect_app_url(String next_redirect_app_url) {
        this.next_redirect_app_url = next_redirect_app_url;
    }

    public void setNext_redirect_mobile_url(String next_redirect_mobile_url) {
        this.next_redirect_mobile_url = next_redirect_mobile_url;
    }

    public void setNext_redirect_pc_url(String next_redirect_pc_url) {
        this.next_redirect_pc_url = next_redirect_pc_url;
    }

    public void setAndroid_app_scheme(String android_app_scheme) {
        this.android_app_scheme = android_app_scheme;
    }

    public void setIos_app_scheme(String ios_app_scheme) {
        this.ios_app_scheme = ios_app_scheme;
    }
}
