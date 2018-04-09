package com.etsdk.app.huov7.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\3\13 0013.
 */

public class ReplyBean implements Serializable{
    private String id;
    private String article_id;
    private String discuss_id;
    private String form_uid;
    private String to_uid;
    private String content;
    private String ctime;
    private String up_time;
    private String r_status;
    private String form_uname;
    private String to_uname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getDiscuss_id() {
        return discuss_id;
    }

    public void setDiscuss_id(String discuss_id) {
        this.discuss_id = discuss_id;
    }

    public String getForm_uid() {
        return form_uid;
    }

    public void setForm_uid(String form_uid) {
        this.form_uid = form_uid;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUp_time() {
        return up_time;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }

    public String getR_status() {
        return r_status;
    }

    public void setR_status(String r_status) {
        this.r_status = r_status;
    }

    public String getForm_uname() {
        return form_uname;
    }

    public void setForm_uname(String form_uname) {
        this.form_uname = form_uname;
    }

    public String getTo_uname() {
        return to_uname;
    }

    public void setTo_uname(String to_uname) {
        this.to_uname = to_uname;
    }
}
