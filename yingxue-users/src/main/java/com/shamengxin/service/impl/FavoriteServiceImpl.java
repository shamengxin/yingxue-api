package com.shamengxin.service.impl;

import com.shamengxin.entity.Favorite;
import com.shamengxin.mapper.FavoriteMapper;
import com.shamengxin.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 收藏(Favorite)表服务实现类
 *
 * @author makejava
 * @since 2023-09-21 22:53:39
 */
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {
    @Resource
    private FavoriteMapper favoriteMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Favorite queryById(Integer id) {
        return this.favoriteMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param favorite    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Favorite> queryByPage(Favorite favorite, PageRequest pageRequest) {
        long total = this.favoriteMapper.count(favorite);
        return new PageImpl<>(this.favoriteMapper.queryAllByLimit(favorite, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param favorite 实例对象
     * @return 实例对象
     */
    @Override
    public Favorite insert(Favorite favorite) {
        this.favoriteMapper.insert(favorite);
        return favorite;
    }

    /**
     * 修改数据
     *
     * @param favorite 实例对象
     * @return 实例对象
     */
    @Override
    public Favorite update(Favorite favorite) {
        this.favoriteMapper.update(favorite);
        return this.queryById(favorite.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.favoriteMapper.deleteById(id) > 0;
    }

    @Override
    public Favorite findByUidAndVideoId(Integer videoId, Integer uid) {
        return favoriteMapper.findByUidAndVideoId(videoId, uid);

    }

    @Override
    public void deleteByUidAndVideoId(Integer uid, Integer videoId) {
        favoriteMapper.deleteByUidAndVideoId(uid, videoId);
    }
}
