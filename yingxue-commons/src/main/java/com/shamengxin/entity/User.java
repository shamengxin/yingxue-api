package com.shamengxin.entity;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
public class User implements Serializable {

    private static final long serialVersionUID = 2427881869198906744L;
    private Integer id  ;
    private String name;
    private String avatar;
    /**
     * 简介
     */
    private String intro ;
    private String phone;
    /**
     * 是否绑定手机号
     */
    private Object phoneLinked ;
    private String openid;
    /**
     * 是否绑定微信
     */
    private Object wechatLinked ;
    /**
     * 关注数
     */
    private Integer following_count;
    /**
     * 粉丝数
     */
    private Integer followers_count ;
    private Date created_at;
    private Date updated_at ;
    private Date deleted_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getPhoneLinked() {
        return phoneLinked;
    }

    public void setPhoneLinked(Object phoneLinked) {
        this.phoneLinked = phoneLinked;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Object getWechatLinked() {
        return wechatLinked;
    }

    public void setWechatLinked(Object wechatLinked) {
        this.wechatLinked = wechatLinked;
    }

    public Integer getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(Integer following_count) {
        this.following_count = following_count;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
