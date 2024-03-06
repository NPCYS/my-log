package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.entity.LoginUser;
import com.mqz.domin.entity.User;
import com.mqz.mapper.MenuMapper;
import com.mqz.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //如果是管理员就进行权限封装
        if (user.getType().equals(SystemConstants.ADMIN)){
            //查询用户的权限信息
            List<String> permissions = menuMapper.getPermissions(user.getId());
            //封装到LoginUser中
            return new LoginUser(user,permissions);
        }
        return new LoginUser(user,null);
    }
}