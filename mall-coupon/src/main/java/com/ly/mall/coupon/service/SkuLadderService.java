package com.ly.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.coupon.entity.SkuLadderEntity;

import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 19:44:25
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

