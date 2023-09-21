package com.shamengxin.controller;

import com.shamengxin.entity.Video;
import com.shamengxin.service.VideoService;
import com.shamengxin.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class VideoServiceController {

    private VideoService videoService;

    @Autowired
    public VideoServiceController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 发布视频
     * @param video
     * @return
     */
    @PostMapping("publish")
    public Video publish(@RequestBody Video video){
        log.info("接收到的video：{}",video);
        Video videoReturn = videoService.insert(video);
        return videoReturn;
    }

    /**
     * 根据视频id查询视频
     */
    @GetMapping("getvideos")
    public List<VideoVO> getVideos(@RequestParam("ids") List<Integer> ids){
        List<VideoVO> videoVOS = new ArrayList<>();
        log.info("ids:{}",ids);
        ids.forEach(id->{
            VideoVO videoVO = videoService.queryById(id);
            videoVOS.add(videoVO);
        });
        return videoVOS;
    }

}
