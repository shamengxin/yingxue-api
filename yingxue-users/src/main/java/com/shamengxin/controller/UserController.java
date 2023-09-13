package com.shamengxin.controller;

import ch.qos.logback.core.net.ObjectWriter;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.errorprone.annotations.Var;
import com.shamengxin.annotations.RequiredToken;
import com.shamengxin.contants.RedisPre;
import com.shamengxin.entity.User;
import com.shamengxin.service.UserService;
import com.shamengxin.util.ImageUtils;
import com.shamengxin.vo.MsgVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class UserController {

    private StringRedisTemplate stringRedisTemplate;
    private UserService userService;

    @Autowired
    public UserController(StringRedisTemplate stringRedisTemplate, UserService userService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
    }

    /**
     * 已登录的用户信息
     * @param request
     * @return
     */
    @RequiredToken
    @GetMapping("user")
    public User user(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        log.info("获取的用户信息为：{}",JSONUtil.toJsonStr(user));
        return user;
    }

    /**
     * 登录
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
}
