package com.ly.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ly.mall.product.entity.AttrEntity;
import com.ly.mall.product.service.AttrAttrgroupRelationService;
import com.ly.mall.product.service.AttrService;
import com.ly.mall.product.service.CategoryService;
import com.ly.mall.product.vo.AttrGroupRelationVo;
import com.ly.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ly.mall.product.entity.AttrGroupEntity;
import com.ly.mall.product.service.AttrGroupService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.common.utils.R;



/**
 * 属性分组
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-11 16:39:17
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;



    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttr(@PathVariable("catelogId")Long catelogId){
        List<AttrGroupWithAttrsVo> vos=attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }

    @PostMapping("/attr/relation")
    public R AddRelation(@RequestBody List<AttrGroupRelationVo> relationVos){
        attrAttrgroupRelationService.saveBatch(relationVos);
        return R.ok();
    }
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId")Long attrgroupId){
        List<AttrEntity> entities=attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId")Long attrgroupId,@RequestParam Map<String, Object> params){
        PageUtils pageUtils=attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",pageUtils);
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId")  Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long [] path=categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        System.out.println(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRealtion(vos);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
