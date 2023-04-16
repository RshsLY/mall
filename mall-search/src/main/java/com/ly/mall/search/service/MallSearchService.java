package com.ly.mall.search.service;

import com.ly.mall.search.vo.SearchParam;
import com.ly.mall.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);
}
