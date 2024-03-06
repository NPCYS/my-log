package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.UserListDto;
import com.mqz.domin.dto.UserStatusDto;
import com.mqz.domin.entity.User;
import com.mqz.domin.vo.UserInfoVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-04-21 20:23:39
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(UserInfoVo userInfoVo);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto);

    ResponseResult changeStatus(UserStatusDto userStatusDto);
}

