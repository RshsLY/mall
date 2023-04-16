package com.ly.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.mall.product.service.CategoryBrandRelationService;
import com.ly.mall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.common.utils.Query;

import com.ly.mall.product.dao.CategoryDao;
import com.ly.mall.product.entity.CategoryEntity;
import com.ly.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    RedissonClient redissonClient;
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = entities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu)->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort())- (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedissonLock() {


        //创建读锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalogJson-lock");

        RLock rLock = readWriteLock.readLock();

        Map<String, List<Catelog2Vo>> dataFromDb = null;
        try {
            rLock.lock();
            //加锁成功...执行业务
            dataFromDb = getDataFromDb();
        } finally {
            rLock.unlock();
        }


        return dataFromDb;

    }
    private  Map<String, List<Catelog2Vo>> getDataFromDb() {
        //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (!org.springframework.util.StringUtils.isEmpty(catalogJson)) {
            //缓存不为空直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });

            return result;
        }

        System.out.println("查询了数据库");
        synchronized (this){
            String cj = stringRedisTemplate.opsForValue().get("catalogJson");
            if (!org.springframework.util.StringUtils.isEmpty(cj)) {
                //缓存不为空直接返回
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });

                return result;
            }
            /**
             * 将数据库的多次查询变为一次
             */
            List<CategoryEntity> selectList = this.baseMapper.selectList(null);

            //1、查出所有分类
            //1、1）查出所有一级分类
            List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

            //封装数据
            Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                //1、每一个的一级分类,查到这个一级分类的二级分类
                List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

                //2、封装上面的结果
                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());

                        //1、找当前二级分类的三级分类封装成vo
                        List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());

                        if (level3Catelog != null) {
                            List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                                //2、封装成指定格式
                                Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                                return category3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(category3Vos);
                        }

                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }

                return catelog2Vos;
            }));

            //3、将查到的数据放入缓存,将对象转为json
            String valueJson = JSON.toJSONString(parentCid);
            stringRedisTemplate.opsForValue().set("catalogJson", valueJson, 1, TimeUnit.DAYS);
            return parentCid;
        }


    }
    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths=new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return (Long[]) parentPath.toArray(new Long[parentPath.size()]);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = {"category"},key ="'getLevel1Categorys'" ),
            @CacheEvict(value = {"category"},key ="'getCatalogJson'" )
    })
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    @Override
    @Cacheable(value = {"category"},key = "#root.method.name")
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",0));

    }


    @Override
    @Cacheable(value = {"category"},key = "#root.method.name",sync = true)
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> collect=new HashMap<>();
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        collect = level1Categorys.stream().collect(Collectors.toMap(k -> {
            return k.getCatId().toString();
        }, v -> {
            List<CategoryEntity> c2 = getParent_cid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (c2 != null) {
                catelog2Vos = c2.stream().map(i -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, i.getCatId().toString(), i.getName());
                    List<CategoryEntity> c3 = getParent_cid(selectList, i.getCatId());
                    if (c3 != null) {
                        List<Catelog2Vo.Category3Vo> category3VoList = c3.stream().map(i3 -> {
                            Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(i.getCatId().toString(), i3.getCatId().toString(), i3.getName());
                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(category3VoList);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return collect;
    }





    private List<CategoryEntity> getParent_cid(List<CategoryEntity>categoryEntities,Long cid) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(i -> {
            return i.getParentCid() == cid;
        }).collect(Collectors.toList());
        return  collect;
    }

    private  List<Long> findParentPath(Long catelogId,List<Long> paths){
        System.out.println(catelogId);
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0) findParentPath(byId.getParentCid(),paths);
        return paths;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        return all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid()==root.getCatId();
        }).map((menu)->{
            menu.setChildren(getChildrens(menu,all));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort())- (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
    }
}