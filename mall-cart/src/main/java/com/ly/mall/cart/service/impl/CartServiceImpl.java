package com.ly.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.mall.cart.feign.ProductFeignService;
import com.ly.mall.cart.interceptor.CartInterceptor;
import com.ly.mall.cart.service.CartService;
import com.ly.mall.cart.vo.CartItemVo;
import com.ly.mall.cart.vo.CartVo;
import com.ly.mall.cart.vo.SkuInfoVo;
import com.ly.mall.cart.vo.UserInfoTo;
import com.ly.mall.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.ly.mall.common.constant.CartConstant.CART_PREFIX;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ThreadPoolExecutor executor;
    @Override
    public CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations<String, Object, Object> ops = getops();
        String o = (String) ops.get(skuId.toString());
        if(StringUtils.isEmpty(o)){
            CartItemVo cartItemVo = new CartItemVo();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                R info = productFeignService.info(skuId);
                SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItemVo.setCheck(true);
                cartItemVo.setCount(num);
                cartItemVo.setImage(skuInfo.getSkuDefaultImg());
                cartItemVo.setTitle(skuInfo.getSkuTitle());
                cartItemVo.setPrice(skuInfo.getPrice());
            },executor);

            CompletableFuture<Void> getSkuSaleAttr = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttrValues(skuSaleAttrValues);
            }, executor);
            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttr).get();
            ops.put(skuId.toString(), JSON.toJSONString(cartItemVo));
            return cartItemVo;
        }
        else{
            CartItemVo cartItemVo1  = JSON.parseObject(o, CartItemVo.class);
            cartItemVo1.setCount(cartItemVo1.getCount()+num);
            ops.put(skuId.toString(), JSON.toJSONString(cartItemVo1));
            return cartItemVo1;
        }



    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getops();

        String redisValue = (String) cartOps.get(skuId.toString());

        CartItemVo cartItemVo = JSON.parseObject(redisValue, CartItemVo.class);

        return cartItemVo;
    }
    @Override
    public void clearCartInfo(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        //查询购物车里面的商品
        CartItemVo cartItem = getCartItem(skuId);
        //修改商品状态
        cartItem.setCheck(checked == 1?true:false);

        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);

        BoundHashOperations<String, Object, Object> cartOps = getops();
        cartOps.put(skuId.toString(),redisValue);
    }

    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {

        //查询购物车里面的商品
        CartItemVo cartItem = getCartItem(skuId);
        cartItem.setCount(num);

        BoundHashOperations<String, Object, Object> cartOps = getops();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),redisValue);
    }

    /**
     * 删除购物项
     * @param skuId
     */
    @Override
    public void deleteIdCartInfo(Integer skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getops();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItemVo> getUserCartItems() {
        List<CartItemVo> cartItemVoList = new ArrayList<>();
        //获取当前用户登录的信息
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        //如果用户未登录直接返回null
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            //获取购物车项
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //获取所有的
            List<CartItemVo> cartItems = getCartItems(cartKey);
//            if (cartItems == null) {
//                throw new CartExceptionHandler();
//            }
            //筛选出选中的
            if(cartItems!=null){
                cartItemVoList = cartItems.stream()
                        .filter(items -> items.getCheck())
                        .map(item -> {
                            //更新为最新的价格（查询数据库）
                            BigDecimal price = productFeignService.getPrice(item.getSkuId());
                            item.setPrice(price);
                            return item;
                        })
                        .collect(Collectors.toList());
            }

        }

        return cartItemVoList;

    }


    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {

        CartVo cartVo = new CartVo();
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //1、登录
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //临时购物车的键
            String temptCartKey = CART_PREFIX + userInfoTo.getUserKey();

            //2、如果临时购物车的数据还未进行合并
            List<CartItemVo> tempCartItems = getCartItems(temptCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据需要进行合并操作
                for (CartItemVo item : tempCartItems) {
                    addToCart(item.getSkuId(),item.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }

            //3、获取登录后的购物车数据【包含合并过来的临时购物车的数据和登录后购物车的数据】
            List<CartItemVo> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);

        } else {
            //没登录
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车里面的所有购物项
            List<CartItemVo> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);
        }

        return cartVo;
    }
    private List<CartItemVo> getCartItems(String cartKey) {
        //获取购物车里面的所有商品
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);

        Map<Object, Object> entries = operations.entries();
        if (entries != null && entries.size() > 0) {
            List<CartItemVo> cartItemVoStream=new ArrayList<>();
            entries.forEach((k,v)->{
                String str = (String) v;
                CartItemVo cartItem = JSON.parseObject(str, CartItemVo.class);
                cartItem.setSkuId(new Long(k.toString()));
                cartItemVoStream.add(cartItem);
            });

            return cartItemVoStream;
        }
        return null;

    }
    private BoundHashOperations<String, Object, Object> getops() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        String cartKey="";
        if(userInfoTo.getUserId()!=null) cartKey=CART_PREFIX+userInfoTo.getUserId();
        else cartKey=CART_PREFIX+userInfoTo.getUserKey();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(cartKey);
        return ops;
    }
}
