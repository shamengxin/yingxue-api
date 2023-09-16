package com.shamengxin.controller;

import cn.hutool.core.util.RandomUtil;
import com.shamengxin.contants.RedisPre;
import com.shamengxin.vo.MsgVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class SMSController {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public SMSController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("captchas")
    public void captchas(@RequestBody MsgVO msgVO){
        //1. 获取手机号
        String phone = msgVO.getPhone();
        log.info("发送短信的手机号: {}",phone);

        //2. 判断此手机号的验证码是否在有效期
        String timeoutKey = RedisPre.TIMEOUT + phone;
        if (stringRedisTemplate.hasKey(timeoutKey)) {
            throw new RuntimeException("提示: 不允许重复发送！");
        }
        //2. 生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        log.info("验证码: {}",code);
        //3. 将对应验证码放入redis中
        String phoneKey = RedisPre.PHONE + phone;
        stringRedisTemplate.opsForValue().set(phoneKey,code,10, TimeUnit.MINUTES);

        //4.如果验证码在有效期内，不允许重新发送
        stringRedisTemplate.opsForValue().set(timeoutKey,"true",60,TimeUnit.SECONDS);
    }

}
