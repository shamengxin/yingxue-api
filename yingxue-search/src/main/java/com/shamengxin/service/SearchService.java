package com.shamengxin.service;

import java.util.Map;

public interface SearchService {
    Map<String, Object> search(String q, Integer page, Integer rows);
}
