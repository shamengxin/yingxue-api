package com.shamengxin.service;


import com.shamengxin.entity.Video;
import com.shamengxin.vo.VideoDetail;
import com.shamengxin.vo.VideoVO;

import java.util.List;

/**
 * 视频(Video)表服务接口
 *
 * @author makejava
 * @since 2023-09-12 23:20:34
 */
public interface VideoService {


    Video insert(Video video);

    List<VideoVO> queryByPage(Integer page, Integer per_page);

    List<VideoVO> queryByCategory(Integer page, Integer rows, Integer categoryId);

    VideoDetail detail(Integer videoId, String token);
}
