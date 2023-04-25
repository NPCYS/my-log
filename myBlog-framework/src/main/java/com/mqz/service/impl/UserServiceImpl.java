package com.mqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.User;
import com.mqz.domin.vo.UserInfoVo;
import com.mqz.mapper.UserMapper;
import com.mqz.service.UserService;
import com.mqz.utils.BeanCopyUtils;
import com.mqz.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-21 20:23:39
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //找到对应的用户信息并封装
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(UserInfoVo userInfoVo) {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        user.setAvatar(userInfoVo.getAvatar());
        user.setNickName(userInfoVo.getNickName());
        user.setSex(userInfoVo.getSex());
        updateById(user);
        return ResponseResult.okResult();
    }
}
