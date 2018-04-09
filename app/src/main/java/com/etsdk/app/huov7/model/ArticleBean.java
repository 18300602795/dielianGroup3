package com.etsdk.app.huov7.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\3\7 0007.
 */

public class ArticleBean implements Serializable {
    private String article_id;
    private String mem_id;
    private String portrait;
    private String nickname;
    private String title;
    private String contents;
    private String like_number;
    private String comments_number;
    private String browse_number;
    private String image_url;
    private String publish_time;
    private String p_status;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }

    public String getComments_number() {
        return comments_number;
    }

    public void setComments_number(String comments_number) {
        this.comments_number = comments_number;
    }

    public String getBrowse_number() {
        return browse_number;
    }

    public void setBrowse_number(String browse_number) {
        this.browse_number = browse_number;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }
}
