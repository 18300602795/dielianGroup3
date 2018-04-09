package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by Administrator on 2018\3\7 0007.
 */

public class ArticleList {
    private int code;
    private String msg;
    private List<ArticleBean> data;

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

    public List<ArticleBean> getData() {
        return data;
    }

    public void setData(List<ArticleBean> data) {
        this.data = data;
    }
}
