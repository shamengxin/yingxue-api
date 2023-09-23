package com.shamengxin.service;

import com.shamengxin.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 评论(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-09-22 09:16:39
 */
public interface CommentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Comment queryById(Integer id);

    /**
     * 分页查询
     *
     * @param comment 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Comment> queryByPage(Comment comment, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    Comment insert(Comment comment);

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    Comment update(Comment comment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Long findByVideoIdCounts(Integer videoId);

    List<Comment> findByVideoId(Integer videoId, Integer page, Integer rows);

    List<Comment> queryByParentId(Integer id);

}
