package com.shamengxin.vo;

import com.shamengxin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetail {

    private Integer id;

    // 标题
    private String title;

    // 分类
    private String category;

    // 播放连接
    private String link;

    // 创建时间
    private Date createdAt;

    // 修改时间
    private Date updatedAt;

    // up主
    private User uploader;

    // 播放次数
    private Integer playsCount;

    // 点赞次数
    private Integer LikesCount;

    // 是否点赞
    private Boolean liked;

    // 是否不喜欢
    private Boolean disliked;

    // 是否收藏
    private Boolean favorite;

}
