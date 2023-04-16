package com.ly.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.to.OrderTo;
import com.ly.mall.common.to.mq.StockLockedTo;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.ware.entity.WareSkuEntity;
import com.ly.mall.ware.vo.SkuHasStockVo;
import com.ly.mall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 20:00:01
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo orderTo);
}

