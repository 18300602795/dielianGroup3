package com.etsdk.app.huov7.model;

/**
 * Created by Administrator on 2018\3\27 0027.
 */

public class GuildModel {
    private String name;
    private String id;
    private String announcement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }
}
