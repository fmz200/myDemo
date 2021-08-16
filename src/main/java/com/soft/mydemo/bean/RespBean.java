package com.soft.mydemo.bean;

import lombok.Data;

/**
 * Created by fmz200 on 2021/08/07
 */
@Data
public class RespBean {
    private String status;
    private String msg;

    public RespBean() {
    }

    public RespBean(String status, String msg) {

        this.status = status;
        this.msg = msg;
    }
}
