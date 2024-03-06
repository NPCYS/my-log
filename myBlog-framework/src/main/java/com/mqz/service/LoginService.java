package com.mqz.service;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult getinfo();

    ResponseResult getRouters();
}
