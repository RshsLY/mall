package com.ly.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.mall.common.utils.PageUtils;
import com.ly.mall.member.entity.MemberEntity;
import com.ly.mall.member.exception.PhoneExistException;
import com.ly.mall.member.exception.UserNameExistException;
import com.ly.mall.member.vo.MemberRegisterVo;
import com.ly.mall.member.vo.MemberUserLoginVo;

import java.util.Map;

/**
 * 会员
 *
 * @author ly
 * @email 846844910@qq.com
 * @date 2022-12-13 19:50:38
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegisterVo vo);
    public void checkPhoneUnique(String phone) throws PhoneExistException;

    public void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemberUserLoginVo vo);
}

