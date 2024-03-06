package com.mqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.domin.entity.RoleMenu;
import com.mqz.mapper.RoleMenuMapper;
import com.mqz.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-07 15:45:07
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
