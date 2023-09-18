package com.shamengxin.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shamengxin.entity.Video;
import com.shamengxin.utils.JSONUtils;
import com.shamengxin.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sound.midi.Receiver;
import java.io.IOException;

@Slf4j
@Component
public class VideoConsumer {
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public VideoConsumer(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue,exchange = @Exchange(name = "videos",type = "fanout")))
    public void receive(String message) throws IOException {
        log.info("MQ接收的video信息为：{}",message);
        // 1.将mq中的video的json格式转化为对象
        Video video = new ObjectMapper().readValue(message, Video.class);
        VideoVO videoVo = new VideoVO();
        BeanUtils.copyProperties(video, videoVo)   ;

        IndexRequest indexRequest = new IndexRequest("video");
        indexRequest.id(videoVo.getId().toString())
                .type("_doc")
                .source(JSONUtils.writeValueAsString(videoVo), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info("video信息录入ES的状态为：{}",indexResponse.status());

    }

}
