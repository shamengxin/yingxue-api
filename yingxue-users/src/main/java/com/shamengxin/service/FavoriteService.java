package com.shamengxin.service;

import com.shamengxin.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 收藏(Favorite)表服务接口
 *
 * @author makejava
 * @since 2023-09-21 22:53:39
 */
public interface FavoriteService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Favorite queryById(Integer id);

    /**
     * 分页查询
     *
     * @param favorite 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Favorite> queryByPage(Favorite favorite, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param favorite 实例对象
     * @return 实例对象
     */
    Favorite insert(Favorite favorite);

    /**
     * 修改数据
     *
     * @param favorite 实例对象
     * @return 实例对象
     */
    Favorite update(Favorite favorite);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Favorite findByUidAndVideoId(Integer videoId, Integer uid);

    void deleteByUidAndVideoId(Integer id, Integer videoId);

}
