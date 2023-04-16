package com.ly.mall.ware.service.impl;

import com.ly.mall.common.constant.WareConstant;
import com.ly.mall.ware.entity.PurchaseDetailEntity;
import com.ly.mall.ware.service.PurchaseDetailService;
import com.ly.mall.ware.service.WareSkuService;
import com.ly.mall.ware.vo.MergeVo;
import com.ly.mall.ware.vo.PurchaseDoneVo;
import com.ly.mall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.common.utils.Query;

import com.ly.mall.ware.dao.PurchaseDao;
import com.ly.mall.ware.entity.PurchaseEntity;
import com.ly.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {


    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        Long id=mergeVo.getPurchaseId();
        if(purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            id = purchaseEntity.getId();
        }
        //TODO 确认采购单是0或者1才可以合并
        List<Long> items = mergeVo.getItems();
        Long finalId = id;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i);
            purchaseDetailEntity.setPurchaseId(finalId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailsStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
        PurchaseEntity purchaseEntity1 = new PurchaseEntity();
        purchaseEntity1.setId(finalId);
        purchaseEntity1.setUpdateTime(new Date());
        this.updateById(purchaseEntity1);
    }

    @Override
    public void received(List<Long> ids) {
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(i -> {
            if (i.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || i.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            } else return false;
        }).map(i->{
            i.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            return i;
        }).collect(Collectors.toList());
        this.updateBatchById(collect);
        collect.forEach(i->{
            List<PurchaseDetailEntity> purchaseDetailEntities=purchaseDetailService.listDetailByPurchaseId(i.getId());
            List<PurchaseDetailEntity> collect1 = purchaseDetailEntities.stream().map(e -> {
                PurchaseDetailEntity detail = new PurchaseDetailEntity();
                detail.setId(e.getId());
                detail.setStatus(WareConstant.PurchaseDetailsStatusEnum.BUYING.getCode());
                return detail;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });
    }

    @Override
    @Transactional
    public void done(PurchaseDoneVo purchaseDoneVo) {
        Long id = purchaseDoneVo.getId();

        Boolean flag=true;
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> updates=new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if(item.getStatus()==WareConstant.PurchaseDetailsStatusEnum.HASERROR.getCode()) {
                flag=false;
                detailEntity.setStatus(item.getStatus());
            }else{
                detailEntity.setStatus(WareConstant.PurchaseDetailsStatusEnum.FINISHED.getCode());
                PurchaseDetailEntity byId = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getSkuId(),byId.getWareId(),byId.getSkuNum());
            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);

        }
        purchaseDetailService.updateBatchById(updates);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        if(flag)  purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISHED.getCode());
        else purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        this.updateById(purchaseEntity);


    }

}