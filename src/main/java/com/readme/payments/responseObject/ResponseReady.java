package com.readme.payments.responseObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseReady {

    private String partner_order_id;

    private String tid;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;
}
