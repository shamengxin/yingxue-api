package com.shamengxin.feignclients;

import com.shamengxin.entity.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("API-VIDEOS")
public interface VideosClient {

    @PostMapping("publish")
    Video publish(@RequestBody Video video);
}
