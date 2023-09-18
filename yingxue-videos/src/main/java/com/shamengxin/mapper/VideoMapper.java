package com.shamengxin.mapper;


import com.shamengxin.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频(Video)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-12 23:20:34
 */
@Mapper
public interface VideoMapper {


    void insert(Video video);

    List<Video> queryByPage(@Param("start") Integer start, @Param("rows") Integer rows);

    List<Video> queryByCategory(@Param("start") Integer start, @Param("rows") Integer rows, @Param("categoryId") Integer categoryId);

    Video queryById(Integer videoId);
}

