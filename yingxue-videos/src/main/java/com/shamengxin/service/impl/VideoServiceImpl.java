package com.shamengxin.service.impl;


import com.shamengxin.entity.Video;
import com.shamengxin.mapper.VideoMapper;
import com.shamengxin.service.VideoService;
import com.shamengxin.utils.JSONUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Date;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public Video insert(Video video) {
        video.setCreatedAt(new Date());
        video.setUpdatedAt(new Date());
        videoMapper.insert(video);

        // 利用mq异步处理，将视频信息写到redis中
        rabbitTemplate.convertAndSend("videos","", JSONUtils.writeValueAsString(video));
        return video;
    }
}
