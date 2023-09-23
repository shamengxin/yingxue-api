package com.shamengxin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentVO {

    private Integer id;

    private String content;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("parent_id")
    private Integer parentId;

    private Reviewer reviewer;

    @JsonProperty("sub_comments")
    private List<CommentVO> subComments;

}
