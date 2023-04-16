package com.ly.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.coupon.entity.CouponSpuCategoryRelationEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 19:44:25
 */
public interface CouponSpuCategoryRelationService extends IService<CouponSpuCategoryRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

