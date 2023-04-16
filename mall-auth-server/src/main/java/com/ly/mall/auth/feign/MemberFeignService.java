package com.ly.mall.auth.feign;

import com.ly.mall.common.utils.R;
import com.ly.mall.auth.vo.UserLoginVo;
import com.ly.mall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient("mall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/regist")
    public R regist(@RequestBody UserRegisterVo vo);
    @PostMapping(value = "/member/member/login")
    public R login(@RequestBody UserLoginVo vo);
}
