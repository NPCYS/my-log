package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.MenuListDto;
import com.mqz.domin.entity.Menu;
import com.mqz.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult getAllMenu(MenuListDto menuListDto){
        return menuService.getAllMenu(menuListDto);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable Long id){
        return menuService.deleteMenu(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getOneMenu(@PathVariable Long id){
        return menuService.getOneMenu(id);
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        return menuService.treeSelect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id){
        return menuService.roleMenuTreeSelect(id);
    }

}
