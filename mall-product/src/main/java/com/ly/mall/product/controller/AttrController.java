package com.ly.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ly.mall.product.entity.ProductAttrValueEntity;
import com.ly.mall.product.service.ProductAttrValueService;
import com.ly.mall.product.vo.AttrRespVo;
import com.ly.mall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ly.mall.product.service.AttrService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.common.utils.R;



/**
 * 商品属性
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-11 16:39:17
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

//    @GetMapping("/base/list/{catelogId}")
//    public R baseAttrList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
//        PageUtils pageUtils=attrService.queryBaseAttrPage(params,catelogId, type);
//        return R.ok().put("page",pageUtils);
//    }

    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrListForSpu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> data=productAttrValueService.baseAttrListForSpu(spuId);
        return R.ok().put("data",data);
    }


    @GetMapping("/{attrType}/list/{catelogId}")
    public R saleAttrList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,@PathVariable("attrType") String type){
        PageUtils pageUtils=attrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page",pageUtils);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attrRespVo=attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateAttr(attrVo);

        return R.ok();
    }
    @PostMapping("/update/{spuId}")
    // @RequiresPermissions("product:attr:update")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,@RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
