package com.ly.mall.seckill.service;


import com.ly.mall.seckill.to.SeckillSkuRedisTo;

import java.util.List;


public interface SeckillService {

    /**
     * 上架三天需要秒杀的商品
     */
    void uploadSeckillSkuLatest3Days();
    StringBuilder
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSeckilInfo(Long skuId);

    String kill(String killId, String key, Integer num) throws InterruptedException;
}
