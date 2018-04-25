package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by Administrator on 2018\3\27 0027.
 */

public class MemberList {
    private int code;
    private String msg;
    private List<MemberModel> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MemberModel> getData() {
        return data;
    }

    public void setData(List<MemberModel> data) {
        this.data = data;
    }
}
