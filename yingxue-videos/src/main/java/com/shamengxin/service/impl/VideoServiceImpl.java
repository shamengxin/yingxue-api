package com.shamengxin.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shamengxin.contants.RedisPre;
import com.shamengxin.entity.Category;
import com.shamengxin.entity.Favorite;
import com.shamengxin.entity.User;
import com.shamengxin.entity.Video;
import com.shamengxin.feignclients.CategoriesClient;
import com.shamengxin.feignclients.UsersClient;
import com.shamengxin.mapper.VideoMapper;
import com.shamengxin.service.VideoService;
import com.shamengxin.utils.JSONUtils;
import com.shamengxin.vo.VideoDetail;
import com.shamengxin.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 视频(Video)表服务实现类
 *
 * @author makejava
 * @since 2023-09-12 23:20:34
 */
@Slf4j
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    private VideoMapper videoMapper;

    private RabbitTemplate rabbitTemplate;

    private UsersClient usersClient;

    private CategoriesClient categoriesClient;

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public VideoServiceImpl(VideoMapper videoMapper, RabbitTemplate rabbitTemplate, UsersClient usersClient, CategoriesClient categoriesClient, StringRedisTemplate stringRedisTemplate) {
        this.videoMapper = videoMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.usersClient = usersClient;
        this.categoriesClient = categoriesClient;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public Video insert(Video video) {
        video.setCreatedAt(new Date());
        video.setUpdatedAt(new Date());
        videoMapper.insert(video);

        // 利用mq异步处理，将视频信息写到redis中
        rabbitTemplate.convertAndSend("videos", "", JSONUtils.writeValueAsString(video));
        return video;
    }

    @Override
    public List<VideoVO> queryByPage(Integer offset, Integer limit) {
        Integer start = (offset - 1) * limit;
        // 查询所有视频对象
        List<Video> videos = videoMapper.queryByPage(start, limit);
        return getList(videos);
    }

    @Override
    public List<VideoVO> queryByCategory(Integer page, Integer rows, Integer categoryId) {
        Integer start = (page - 1) * rows;
        List<Video> videos = videoMapper.queryByCategory(start,rows,categoryId);
        return getList(videos);
    }

    @Override
    public VideoDetail detail(Integer videoId, String token) {

        VideoDetail videoDetail = new VideoDetail();

        // 1.获取video对象
        Video video = videoMapper.queryById(videoId);
        // 2.将video对象中与videoDetail对象中相同的属性进行赋值
        BeanUtils.copyProperties(video,videoDetail);
        // 3.获取upload信息
        User user = usersClient.queryById(video.getUid());
        videoDetail.setUploader(user);
        // 4.获取category信息
        Category category = categoriesClient.findById(video.getCategoryId());
        videoDetail.setCategory(category.getName());

        // 5.视频播放次数
        videoDetail.setPlaysCount(10);
        String PlayedCount = stringRedisTemplate.opsForValue().get(RedisPre.VIDEO_PLAYED_COUNT + videoId);
        if (!ObjectUtils.isEmpty(PlayedCount)){
            videoDetail.setPlaysCount(Integer.valueOf(PlayedCount));
        }

        // 6.视频点赞次数
        videoDetail.setLikesCount(10);
        String likedCount = stringRedisTemplate.opsForValue().get(RedisPre.VIDEO_LIKED_COUNT + videoId);
        if (!ObjectUtils.isEmpty(likedCount)){
            videoDetail.setLikesCount(Integer.valueOf(likedCount));
        }

        // 7.判断用户是否登录
        if(!ObjectUtils.isEmpty(token)){
            String UserJson = stringRedisTemplate.opsForValue().get(RedisPre.SESSION + token);
            User userLogin = new User();
            try {
               userLogin = new ObjectMapper().readValue(UserJson,User.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // a.是否点赞
            videoDetail.setLiked(stringRedisTemplate.opsForSet().isMember(RedisPre.USER_LIKE_+userLogin.getId(),videoId.toString()));
            // b.是否不喜欢
            videoDetail.setDisliked(stringRedisTemplate.opsForSet().isMember(RedisPre.USER_DISLIKED_+user.getId(),videoId.toString()));
            // c.是否收藏
            Favorite favorite = usersClient.favorite(videoId,user.getId());
            if (!ObjectUtils.isEmpty(favorite)){
                videoDetail.setFavorite(true);
            }
        }



        return videoDetail;
    }

    @Override
    public VideoVO queryById(Integer id) {

        Video video = videoMapper.queryById(id);
        return this.getVideoVO(video);
    }

    // 将video对象转化为videoVO对象
    private List<VideoVO> getList(List<Video> videos) {
        List<VideoVO> videoVOS = new ArrayList<>();
        videos.forEach(video -> {

            VideoVO videoVO = getVideoVO(video);

            videoVOS.add(videoVO);
        });
        return videoVOS;
    }

    // 将video对象转化为videoVO对象
    private VideoVO getVideoVO(Video video) {
        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);

        // 调用用户服务获取Uploader
        User user = usersClient.queryById(video.getUid());
        log.info("根据id查询到的用户信息为：{}", user);
        videoVO.setUploader(user.getName());

        // 调用类别服务获取categories
        Category category = categoriesClient.findById(video.getCategoryId());
        log.info("根据id查询到的类别信息为：{}", category);
        videoVO.setCategory(category.getName());

        // 设置点赞数
        videoVO.setLikes(0);

        return videoVO;
    }
}
