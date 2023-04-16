package com.ly.mall.search.feign;


import com.ly.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-product")
public interface ProductFeignService {
    @RequestMapping("product/attr/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId);
}
