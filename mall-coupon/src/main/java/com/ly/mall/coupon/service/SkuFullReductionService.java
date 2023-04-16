package com.ly.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.to.SkuReductionTo;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 19:44:25
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

