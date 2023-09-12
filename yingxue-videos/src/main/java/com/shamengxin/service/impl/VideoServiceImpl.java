package com.shamengxin.service.impl;


import com.shamengxin.mapper.VideoMapper;
import com.shamengxin.service.VideoService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * 视频(Video)表服务实现类
 *
 * @author makejava
 * @since 2023-09-12 23:20:34
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;


}
