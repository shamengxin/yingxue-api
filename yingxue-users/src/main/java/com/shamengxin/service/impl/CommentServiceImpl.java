package com.shamengxin.service.impl;

import com.shamengxin.entity.Comment;
import com.shamengxin.mapper.CommentMapper;
import com.shamengxin.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-09-22 09:16:39
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Comment queryById(Integer id) {
        return this.commentMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param comment     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Comment> queryByPage(Comment comment, PageRequest pageRequest) {
        long total = this.commentMapper.count(comment);
        return new PageImpl<>(this.commentMapper.queryAllByLimit(comment, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment insert(Comment comment) {
        this.commentMapper.insert(comment);
        return comment;
    }

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment update(Comment comment) {
        this.commentMapper.update(comment);
        return this.queryById(comment.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.commentMapper.deleteById(id) > 0;
    }

    @Override
    public Long findByVideoIdCounts(Integer videoId) {
        return commentMapper.findByVideoIdCounts(videoId);
    }

    @Override
    public List<Comment> findByVideoId(Integer videoId, Integer page, Integer rows) {
        Integer start = (page - 1) * rows;
        return commentMapper.findByVideoId(videoId,start,rows);
    }

    @Override
    public List<Comment> queryByParentId(Integer id) {
        return commentMapper.queryByParentId(id);
    }
}
