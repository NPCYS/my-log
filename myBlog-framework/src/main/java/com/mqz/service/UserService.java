package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-04-21 20:23:39
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();
}

