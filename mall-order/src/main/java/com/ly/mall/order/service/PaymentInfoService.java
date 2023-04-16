package com.ly.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 19:56:44
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

