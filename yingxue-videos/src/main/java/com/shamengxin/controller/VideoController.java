package com.shamengxin.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shamengxin.contants.RedisPre;
import com.shamengxin.entity.Comment;
import com.shamengxin.entity.User;
import com.shamengxin.feignclients.UsersClient;
import com.shamengxin.service.VideoService;
import com.shamengxin.vo.VideoDetail;
import com.shamengxin.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    public VideoController(VideoService videoService, UsersClient usersClient, StringRedisTemplate stringRedisTemplate) {
        this.videoService = videoService;
        this.usersClient = usersClient;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("videos/{videoId}/comments")
    public void comments(@PathVariable("videoId") Integer videoId,
                         @RequestBody Comment comment, HttpServletRequest request) throws JsonProcessingException {
        String token = request.getParameter("token");
        String key = RedisPre.SESSION+token;
        String userJson= stringRedisTemplate.opsForValue().get(key);
        User user = new ObjectMapper().readValue(userJson, User.class);
        comment.setVideoId(videoId);
        comment.setUid(user.getId());

        usersClient.comments(comment);

    }

    /**
     * 查询视频评论
     * @param page
     * @param rows
     * @param videoId
     * @return
     */
    @GetMapping("videos/{videoId}/comments")
    public Map<String,Object> comments(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                       @RequestParam(value = "per_page",defaultValue = "5") Integer rows,
                                       @PathVariable("videoId") Integer videoId){
        log.info("视频id：{}",videoId);
        log.info("当前页：{}",page);
        log.info("每页显示记录条数：{}",rows);

        return usersClient.comments(page,rows,videoId);
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

