package com.ly.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.ware.entity.WareInfoEntity;
import com.ly.mall.ware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 20:00:01
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    FareVo getFare(Long addrId);
}

