package com.shamengxin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shamengxin.service.SearchService;
import com.shamengxin.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public SearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Map<String, Object> search(String q, Integer page, Integer rows) {

        Map<String, Object> result = new HashMap<>();

        int start = (page - 1) * rows;

        SearchRequest searchRequest = new SearchRequest("video");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder
                .from(start)
                .size(rows)
                .query(QueryBuilders.termQuery("title", q));
        searchRequest.source(sourceBuilder);

        SearchResponse search = null;
        try {
            // 执行搜索
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 获取查询的条件总数
            long totalHits = search.getHits().getTotalHits();
            List<VideoVO> vos = new ArrayList<>();
            if (totalHits > 0){
                result.put("total_court",totalHits);

                SearchHit[] hits = search.getHits().getHits();
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    log.info("符合条件的结果：{}",sourceAsString);
                    VideoVO videoVO = new ObjectMapper().readValue(sourceAsString, VideoVO.class);
                    vos.add(videoVO);
                }
                result.put("items",vos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
}
