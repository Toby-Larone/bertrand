/*
 * This file is generated by jOOQ.
 */
package uk.co.markg.mimic.db.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Messages implements Serializable {

    private static final long serialVersionUID = 1067005720;

    private Long   messageid;
    private Long   userid;
    private String content;
    private Long   channelid;
    private Long   serverid;

    public Messages() {}

    public Messages(Messages value) {
        this.messageid = value.messageid;
        this.userid = value.userid;
        this.content = value.content;
        this.channelid = value.channelid;
        this.serverid = value.serverid;
    }

    public Messages(
        Long   messageid,
        Long   userid,
        String content,
        Long   channelid,
        Long   serverid
    ) {
        this.messageid = messageid;
        this.userid = userid;
        this.content = content;
        this.channelid = channelid;
        this.serverid = serverid;
    }

    public Long getMessageid() {
        return this.messageid;
    }

    public void setMessageid(Long messageid) {
        this.messageid = messageid;
    }

    public Long getUserid() {
        return this.userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChannelid() {
        return this.channelid;
    }

    public void setChannelid(Long channelid) {
        this.channelid = channelid;
    }

    public Long getServerid() {
        return this.serverid;
    }

    public void setServerid(Long serverid) {
        this.serverid = serverid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Messages (");

        sb.append(messageid);
        sb.append(", ").append(userid);
        sb.append(", ").append(content);
        sb.append(", ").append(channelid);
        sb.append(", ").append(serverid);

        sb.append(")");
        return sb.toString();
    }
}
