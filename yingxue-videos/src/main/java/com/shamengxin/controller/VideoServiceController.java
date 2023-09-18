package com.shamengxin.controller;

import com.shamengxin.entity.Video;
import com.shamengxin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class VideoServiceController {

    private VideoService videoService;

    @Autowired
    public VideoServiceController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("publish")
    public Video publish(@RequestBody Video video){
        log.info("接收到的video：{}",video);
        Video videoReturn = videoService.insert(video);
        return videoReturn;
    }
}
