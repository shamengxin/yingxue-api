package com.shamengxin.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 视频(Video)实体类
 *
 * @author makejava
 * @since 2023-09-12 23:20:34
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Video implements Serializable {
    private static final long serialVersionUID = 603263299062371205L;
    
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 简介
     */
    private String intro;
    /**
     * up主id
     */
    private Integer uid;
    /**
     * 视频封面链接
     */
    private String cover;
    /**
     * 视频播放链接
     */
    private String link;
    /**
     * 分类id
     */
    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;

    //业务属性
    private Integer likes;

    private String category;//类别名称

    private User uploader;//用户up主信息

}

