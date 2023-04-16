package com.ly.mall.search.controller;

import com.ly.mall.search.service.MallSearchService;
import com.ly.mall.search.vo.SearchParam;
import com.ly.mall.search.vo.SearchResult;
import org.elasticsearch.client.license.LicensesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request){
        String queryString = request.getQueryString();
        param.set_queryString(queryString);
        SearchResult o=mallSearchService.search(param);
        model.addAttribute("result",o);
        return "list";
    }
}
