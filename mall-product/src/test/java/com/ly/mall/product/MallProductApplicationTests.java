package com.ly.mall.product;

import com.ly.mall.product.entity.BrandEntity;
import com.ly.mall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MallProductApplicationTests {

    @Autowired
    BrandService brandService;



    @Test
    public void contextLoads() {

//        BrandEntity brandEntity=new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("huawei");
//        brandService.save(brandEntity);

    }


}
