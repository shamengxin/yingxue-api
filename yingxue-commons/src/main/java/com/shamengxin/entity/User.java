package com.shamengxin.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户(User)实体类
 *
 * @author makejava
 * @since 2023-09-12 22:46:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    private static final long serialVersionUID = -70451177358151087L;

    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 头像链接
     */
    private String avatar;
    /**
     * 简介
     */
    private String intro;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 是否绑定手机号
     */
    @JsonProperty("phone_linked")
    private Integer phoneLinked;
    /**
     * 微信openid
     */
    private String openid;
    /**
     * 是否绑定微信
     */
    @JsonProperty("wechat_linked")
    private Integer wechatLinked;
    /**
     * 关注数
     */
    private Integer followingCount;
    /**
     * 粉丝数
     */
    private Integer followersCount;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;

    /**
     * 验证码
     */
    private String captcha;


}
