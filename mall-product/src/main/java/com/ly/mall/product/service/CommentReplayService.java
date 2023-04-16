package com.ly.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-11 16:39:17
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

