package com.etsdk.app.huov7.model;


import java.util.List;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GuildHeader {
    private List<GuildModel> guild;
    private MemberModel president;
    private List<MemberModel> members;

    public List<GuildModel> getGuild() {
        return guild;
    }

    public void setGuild(List<GuildModel> guild) {
        this.guild = guild;
    }

    public MemberModel getPresident() {
        return president;
    }

    public void setPresident(MemberModel president) {
        this.president = president;
    }

    public List<MemberModel> getMembers() {
        return members;
    }

    public void setMembers(List<MemberModel> members) {
        this.members = members;
    }
}