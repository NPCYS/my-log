package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.RoleListDto;
import com.mqz.domin.dto.RoleStatusDto;
import com.mqz.domin.entity.Role;
import com.mqz.domin.vo.PageVo;
import com.mqz.domin.vo.ReturnRoleVo;
import com.mqz.mapper.RoleMapper;
import com.mqz.service.RoleService;
import com.mqz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 11:12:13
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Long> getRoleId(Long userId) {
        return roleMapper.getRole(userId);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.like(StringUtils.hasText(roleListDto.getRoleName()),Role::getRoleName,roleListDto.getRoleName());
        roleLambdaQueryWrapper.eq(StringUtils.hasText(roleListDto.getStatus()),Role::getStatus,roleListDto.getStatus());
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = page(rolePage, roleLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changerStatus(RoleStatusDto roleStatusDto) {
        Role role = getById(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = this.getById(id);
        ReturnRoleVo returnRoleVo = BeanCopyUtils.copyBean(role, ReturnRoleVo.class);
        return ResponseResult.okResult(returnRoleVo);
    }
}
