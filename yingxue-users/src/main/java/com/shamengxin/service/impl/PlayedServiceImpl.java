package com.shamengxin.service.impl;

import com.shamengxin.entity.Played;
import com.shamengxin.mapper.PlayedMapper;
import com.shamengxin.service.PlayedService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 播放历史(Played)表服务实现类
 *
 * @author makejava
 * @since 2023-09-20 22:42:23
 */
@Service("playedService")
public class PlayedServiceImpl implements PlayedService {
    @Resource
    private PlayedMapper playedMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Played queryById(Integer id) {
        return this.playedMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param played 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Played> queryByPage(Played played, PageRequest pageRequest) {
        long total = this.playedMapper.count(played);
        return new PageImpl<>(this.playedMapper.queryAllByLimit(played, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param played 实例对象
     * @return 实例对象
     */
    @Override
    public Played insert(Played played) {
        Date date = new Date();
        played.setCreatedAt(date);
        played.setUpdatedAt(date);
        this.playedMapper.insert(played);
        return played;
    }

    /**
     * 修改数据
     *
     * @param played 实例对象
     * @return 实例对象
     */
    @Override
    public Played update(Played played) {
        this.playedMapper.update(played);
        return this.queryById(played.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.playedMapper.deleteById(id) > 0;
    }

    @Override
    public Played findByUidAndVideoId(Integer uid, Integer videoId) {
        return playedMapper.findByUidAndVideoId(uid,videoId);
    }
}
