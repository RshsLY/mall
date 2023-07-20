package com.ly.mall.product.web;

import com.ly.mall.product.service.SkuInfoService;
import com.ly.mall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public  String skuItem(@PathVariable("skuId") Long skuId, Model m){
        SkuItemVo skuItemVo=skuInfoService.item(skuId);
        m.addAttribute("item",skuItemVo);
        return "item";
    }
}
