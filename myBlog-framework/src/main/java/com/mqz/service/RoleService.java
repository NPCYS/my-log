package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.RoleListDto;
import com.mqz.domin.dto.RoleStatusDto;
import com.mqz.domin.entity.Role;

import java.util.ArrayList;
import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 11:12:13
 */
public interface RoleService extends IService<Role> {
    List<Long> getRoleId(Long userId);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);

    ResponseResult changerStatus(RoleStatusDto roleStatusDto);

    ResponseResult getRole(Long id);
}

