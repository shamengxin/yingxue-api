package com.shamengxin.controller;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shamengxin.annotations.RequiredToken;
import com.shamengxin.contants.RedisPre;

import com.shamengxin.entity.Played;
import com.shamengxin.entity.User;
import com.shamengxin.entity.Video;
import com.shamengxin.service.PlayedService;
import com.shamengxin.vo.VideoVO;
import com.shamengxin.feignclients.CategoriesClient;
import com.shamengxin.feignclients.VideosClient;
import com.shamengxin.service.UserService;
import com.shamengxin.util.ImageUtils;
import com.shamengxin.util.OSSUtils;
import com.shamengxin.utils.JSONUtils;
import com.shamengxin.vo.MsgVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class UserController {

    private StringRedisTemplate stringRedisTemplate;
    private UserService userService;
    private VideosClient videosClient;

    private CategoriesClient categoriesClient;

    private PlayedService playedService;


    @Autowired
    public UserController(StringRedisTemplate stringRedisTemplate, UserService userService, VideosClient videosClient, CategoriesClient categoriesClient, PlayedService playedService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
        this.videosClient = videosClient;
        this.categoriesClient = categoriesClient;
        this.playedService = playedService;
    }

    /**
     * 用户取消点赞视频
     * @param videoId
     * @param request
     */
    @DeleteMapping("user/liked/{id}")
    @RequiredToken
    public void disLiked(@PathVariable("id") Integer videoId, HttpServletRequest request){
        // 1.获取用户信息
        User user = (User) request.getAttribute("user");
        log.info("接收到的视频id为：{}",videoId);

        // 2.将视频点赞数减1
        stringRedisTemplate.opsForValue().decrement(RedisPre.VIDEO_LIKED_COUNT+videoId);

        // 3.将点按视频移除
        stringRedisTemplate.opsForSet().remove(RedisPre.USER_LIKE_+ user.getId(),videoId.toString());

        // 4.

    }

    /**
     * 用户点赞视频
     * @param videoId
     * @param request
     */
    @PutMapping("user/liked/{id}")
    @RequiredToken
    public void liked(@PathVariable("id") Integer videoId, HttpServletRequest request){
        // 1.获取用户信息
        User user = (User) request.getAttribute("user");
        log.info("接收到的视频id为：{}",videoId);

        // 2.将视频点赞数加1
        stringRedisTemplate.opsForValue().increment(RedisPre.VIDEO_LIKED_COUNT+videoId);

        // 3.将用户点赞视频列表加入到redis中
        stringRedisTemplate.opsForSet().add(RedisPre.USER_LIKE_+ user.getId(),videoId.toString());

        // 4.

    }

    /**
     * 视频播放
     * @param videoId
     * @param request
     */
    @PutMapping("user/played/{id}")
    public void played(@PathVariable("id") Integer videoId, HttpServletRequest request){

        // 将redis中播放次数+1
        stringRedisTemplate.opsForValue().increment(RedisPre.VIDEO_PLAYED_COUNT+videoId);
        //1.获取登录用户
        User user = getUser(request);
        if (!ObjectUtils.isEmpty(user)){
            Played played = playedService.findByUidAndVideoId(user.getId(),videoId);
            if(!ObjectUtils.isEmpty(played)){
                played.setUpdatedAt(new Date());
                playedService.update(played);
            }else {
                played = new Played();
                played.setUid(user.getId());
                played.setVideoId(videoId);
                played = playedService.insert(played);
                log.info("当前用户的播放记录保存成功，信息为{}", played);
            }
        }


    }



    /**
     * 视频上传
     * @param file
     * @param video
     * @param category_id
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("user/videos")
    @RequiredToken
    public VideoVO publishVideos(MultipartFile file, Video video, Integer category_id, HttpServletRequest request) throws IOException {
        // 1.获取视频原始名称
        String originalFilename = file.getOriginalFilename();
        log.info("接收文件名称：{}", originalFilename);
        log.info("接收到的视频信息：" + new ObjectMapper().writeValueAsString(video));
        log.info("类别id：{}", category_id);
        log.info("文件大小：{}", file.getSize());

        // 2.获取文件的后缀
        String ext = FilenameUtils.getExtension(originalFilename);

        // 3.生成uuid文件名
        String uuidFileName = UUID.randomUUID().toString().replace("-", "");

        // 4.生成文件名
        String newFileName = uuidFileName + "." + ext;

        // 5.上传视频到oss
        String url = OSSUtils.upload(file.getInputStream(), "videos", newFileName);
        log.info("上传成功返回的地址：{}",url);

        // 6.设置封面信息
        String cover = url + "/videos/"+newFileName+"?x-oss-process=video/snapshot,t_3000,f_jpg,w_0,h_0,m_fast,ar_auto";
        log.info("阿里云oss根据url截取视频封面: {}", cover);
        // 7.设置视频信息
        video.setCover(cover);
        video.setLink(url);
        video.setCategoryId(category_id);
        // 8.获取用户信息并设施用户信息
        User user = (User) request.getAttribute("user");
        video.setUid(user.getId());

        video.setCategory(categoriesClient.findById(category_id).getName());
        video.setLikes(0);
        video.setUploader(user.getName());
        // 9.调用视频服务
        Video videoResult = videosClient.publish(video);
        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(videoResult, videoVO);
        log.info("视频发布成功之后放回的视频信息：{}", JSONUtils.writeValueAsString(videoVO));
        return videoVO;

    }

    /**
     * 更新用户信息
     */
    @PatchMapping("user")
    @RequiredToken
    public User update(@RequestBody User user, HttpServletRequest request) throws JsonProcessingException {
        // 1.获取要修改的对象
        User userOld = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        // 2.判断是否要更新手机号
        if (!ObjectUtils.isEmpty(user.getCaptcha())) {
            String phoneKey = RedisPre.PHONE + user.getPhone();
            if (!stringRedisTemplate.hasKey(phoneKey)) throw new RuntimeException("提示：验证码已过期!");
            String redisCaptcha = stringRedisTemplate.opsForValue().get(phoneKey);
            if (!redisCaptcha.equals(user.getCaptcha())) throw new RuntimeException("提示：验证码输入错误!");
            userOld.setPhone(user.getPhone());
        }
        if (!ObjectUtils.isEmpty(user.getName())) userOld.setName(user.getName());
        if (!ObjectUtils.isEmpty(user.getIntro())) userOld.setIntro(user.getIntro());
        // 3.更新用户信息
        userService.update(userOld);

        // 4.修改redis中的用户信息
        stringRedisTemplate.opsForValue().set(RedisPre.SESSION + token, new ObjectMapper().writeValueAsString(userOld), 7, TimeUnit.DAYS);
        return userOld;
    }

    /**
     * 注销
     *
     * @param token
     */
    @DeleteMapping("token")
    public void logout(String token) {
        log.info("当前的token信息：{}", token);
        stringRedisTemplate.delete(RedisPre.SESSION + token);
        log.info("用户已退出!");
    }

    /**
     * 已登录的用户信息
     *
     * @param request
     * @return
     */
    @RequiredToken
    @GetMapping("user")
    public User user(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        log.info("获取的用户信息为：{}", JSONUtil.toJsonStr(user));
        return user;
    }

    /**
     * 登录
     *
     * @param msgVO
     * @param request
     * @return
     */
    @PostMapping("tokens")
    public Map<String, String> login(@RequestBody MsgVO msgVO, HttpServletRequest request) throws JsonProcessingException {
        Map<String, String> result = new HashMap<>();
        // 1.获取手机号和验证码
        String phone = msgVO.getPhone();
        String captcha = msgVO.getCaptcha();
        log.info("手机号：{}，验证码：{}", phone, captcha);

        // 2.判断验证码是否过期及有效
        if (!stringRedisTemplate.hasKey(RedisPre.PHONE + phone)) {
            throw new RuntimeException("验证码已过期!");
        }
        String redisCode = stringRedisTemplate.opsForValue().get(RedisPre.PHONE + phone);
        if (!redisCode.equals(captcha)) {
            throw new RuntimeException("验证码错误!");
        }
        // 3.查询用户是否存在
        User findUser = userService.findPhone(phone);
        // 4.如不存在则新建用户
        if (ObjectUtils.isEmpty(findUser)) {
            User user = new User();
            user.setName(phone);
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user.setPhone(phone);
            user.setIntro("");
            user.setAvatar(ImageUtils.getPhone());
            user.setPhoneLinked(1);
            user.setWechatLinked(0);
            user.setFollowersCount(0);
            user.setFollowingCount(0);
            findUser = userService.insert(user);
        }
        // 5.用户存在，生成token并返回
        String token = request.getSession().getId();
        String tokenKey = RedisPre.SESSION + token;
        log.info("生成的token：{}", token);
        stringRedisTemplate.opsForValue().set(tokenKey, new ObjectMapper().writeValueAsString(findUser), 7, TimeUnit.DAYS);
        result.put("token", token);
        return result;
    }

    // 通过token获取用户信息
    private User getUser(HttpServletRequest request) {

        String token = request.getParameter("token");
        log.info("获取的token为：{}",token);
        String userJson = stringRedisTemplate.opsForValue().get(RedisPre.SESSION + token);
        User user = null;
        try {
           user = new ObjectMapper().readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }
}
