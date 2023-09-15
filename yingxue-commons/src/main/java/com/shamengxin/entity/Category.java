package com.shamengxin.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 分类(Category)实体类
 *
 * @author makejava
 * @since 2023-09-12 22:24:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category implements Serializable {
    private static final long serialVersionUID = -81059372818556118L;
    
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父级分类id
     */
    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;

    /**
     * 孩子，增加属性
     */
    private List<Category> children;

}

