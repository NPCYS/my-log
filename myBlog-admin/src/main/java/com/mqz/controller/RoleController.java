package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.RoleListDto;
import com.mqz.domin.dto.RoleStatusDto;
import com.mqz.domin.entity.Role;
import com.mqz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto){
        return roleService.getRoleList(pageNum,pageSize,roleListDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        return roleService.changerStatus(roleStatusDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable Long id){
        return roleService.getRole(id);
    }
    @GetMapping("/listAllRole")
    public ResponseResult getListAllRole(){
        return null;
    }
}
