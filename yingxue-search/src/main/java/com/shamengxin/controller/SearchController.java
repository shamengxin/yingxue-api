package com.shamengxin.controller;

import com.shamengxin.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("search/videos")
    public Map<String, Object> search(@RequestParam("q") String q,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "pre_page", defaultValue = "5") Integer rows) {
        Map<String, Object> result = searchService.search(q, page, rows);
        return result;
    }
}
