package com.ly.mall.product.fallback;


import com.ly.mall.common.exception.BizCodeEnume;
import com.ly.mall.common.utils.R;
import com.ly.mall.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;



@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),BizCodeEnume.TO_MANY_REQUEST.getMsg());
    }
}
