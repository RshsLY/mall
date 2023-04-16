package com.ly.mall.product.dao;

import com.ly.mall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-11 16:39:17
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRealtion(@Param("entities") List<AttrAttrgroupRelationEntity> collect);
}
