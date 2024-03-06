package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.UserListDto;
import com.mqz.domin.dto.UserStatusDto;
import com.mqz.domin.entity.User;
import com.mqz.domin.vo.PageVo;
import com.mqz.domin.vo.UserInfoVo;
import com.mqz.exception.SystemException;
import com.mqz.mapper.UserMapper;
import com.mqz.service.UserService;
import com.mqz.utils.BeanCopyUtils;
import com.mqz.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.mqz.enums.AppHttpCodeEnum.*;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-21 20:23:39
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Override
    public ResponseResult register(User user) {
        String userName = user.getUserName();
        String email = user.getEmail();
        String nickName = user.getNickName();
        String passWord = user.getPassword();
        //用户名，密码，昵称，邮箱不能为空
        if (!StringUtils.hasText(userName)||!StringUtils.hasText(email)||!StringUtils.hasText(nickName)||!StringUtils.hasText(passWord))
        {
            throw new SystemException(USER_CONTENT_IS_EMPTY);
        }
        //要求用户名，昵称，邮箱不能和数据库中原有的数据重复
        if(userNameExist(userName)){
            throw new SystemException(USERNAME_EXIST);
        }
        if(nickNameExist(nickName)){
            throw new SystemException(NICKNAME_EXIST);
        }
        if(emailExist(email)){
            throw new SystemException(EMAIL_EXIST);
        }
        //密码加密
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        //存储用户
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        Page<User> userPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(StringUtils.hasText(userListDto.getUserName()), User::getUserName,userListDto.getUserName());
        userLambdaQueryWrapper.eq(StringUtils.hasText(userListDto.getPhonenumber()), User::getPhonenumber,userListDto.getPhonenumber());
        userLambdaQueryWrapper.eq(StringUtils.hasText(userListDto.getStatus()), User::getStatus,userListDto.getStatus());
        Page<User> page = page(userPage, userLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(UserStatusDto userStatusDto) {
        String status = userStatusDto.getStatus();
        Long userId = userStatusDto.getUserId();
        User user = getById(userId);
        user.setStatus(status);
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        int count = count(userLambdaQueryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getNickName,nickName);
        int count = count(userLambdaQueryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,userName);
        int count = count(userLambdaQueryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }
}
