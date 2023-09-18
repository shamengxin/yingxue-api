package com.shamengxin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVO {

    private Integer id;

    private String title;

    private String cover;

    private String category;

    private Integer likes;

    private String uploader;

    @JsonProperty("created_at")
    private Date createdAt;
}
