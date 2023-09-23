package com.shamengxin.controller;


import com.shamengxin.feignclients.UsersClient;
import com.shamengxin.service.VideoService;
import com.shamengxin.vo.VideoDetail;
import com.shamengxin.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 视频(Video)表控制层
 *
 * @author makejava
 * @since 2023-09-12 23:20:34
 */
@Slf4j
@RestController
public class VideoController {

    private VideoService videoService;

    private UsersClient usersClient;


    @Autowired
    public VideoController(VideoService videoService, UsersClient usersClient) {
        this.videoService = videoService;
        this.usersClient = usersClient;
    }

    @GetMapping("video/{videoId}/comments")
    public Map<String,Object> comments(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                       @RequestParam(value = "per_page",defaultValue = "5") Integer rows,
                                       @PathVariable("videoId") Integer videoId){
        log.info("视频id：{}",videoId);
        log.info("当前页：{}",page);
        log.info("每页显示记录条数：{}",rows);

        return usersClient.comments(videoId,page,rows);
    }

    /**
     * 获取视频的详细信息
     * @param videoId
     * @param token
     * @return
     */
    @GetMapping("videos/{id}")
    public VideoDetail detail(@PathVariable("id" ) Integer videoId ,@RequestParam(value = "token",required = false) String token){
        return videoService.detail(videoId,token);
    }

    /**
     * 视频列表
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    @GetMapping("videos")
    public List<VideoVO> queryByCategory(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                         @RequestParam(value = "per_page",defaultValue = "5") Integer rows,
                                         @RequestParam(value = "category") Integer categoryId){
        List<VideoVO> vos = videoService.queryByCategory(page,rows,categoryId);
        log.info("符合条件的视频数量：{}",vos.size());
        return vos;
    }


    /**
     * 首页视频推荐
     * @param page
     * @param per_page
     * @return
     */
    @GetMapping("/recommends")
    public List<VideoVO> queryByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                     @RequestParam(value = "per_page",defaultValue = "5") Integer per_page){
        List<VideoVO> videoVOS = videoService.queryByPage(page,per_page);
        log.info("视频推荐列表数量：{}",videoVOS.size());
        return videoVOS;
    }

}

