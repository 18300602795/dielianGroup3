package com.etsdk.app.huov7.iLive.model;

/**
 * 消息体类
 */
public class ChatEntity {

    private String grpSendName;
    private String context;
    private String faceUrl;
    private int type;

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public ChatEntity() {
        // TODO Auto-generated constructor stub
    }


    public String getSenderName() {
        return grpSendName;
    }

    public void setSenderName(String grpSendName) {
        this.grpSendName = grpSendName;
    }


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
