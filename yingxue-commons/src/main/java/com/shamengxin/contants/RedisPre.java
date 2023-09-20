package com.shamengxin.contants;

import org.apache.commons.codec.StringDecoder;

public class RedisPre {
    // 验证码是否过期
    public static final String TIMEOUT="timeout_";
    // 验证码有效期
    public static final String PHONE="phone_";
    // token前缀
    public static final String SESSION="session_";
    // 播放视频总数
    public static final String VIDEO_PLAYED_COUNT="video_played_count_";
    //点赞次数总数
    public static final String VIDEO_LIKED_COUNT="video_liked_count_";
    //用户喜欢视频列表
    public static final String USER_LIKE_="user_like_";

}
