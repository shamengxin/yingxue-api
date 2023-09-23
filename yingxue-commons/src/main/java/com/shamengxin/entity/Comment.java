package com.shamengxin.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 评论(Comment)实体类
 *
 * @author makejava
 * @since 2023-09-22 09:16:55
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment implements Serializable {
    private static final long serialVersionUID = 252973408221171385L;
    
    private Integer id;
    /**
     * 用户id
     */
    private Integer uid;
    /**
     * 视频id
     */
    @JsonProperty("video_id")
    private Integer videoId;
    /**
     * 内容
     */
    private String content;
    /**
     * 父评论id
     */
    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;



}

