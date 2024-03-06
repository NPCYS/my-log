package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.MenuListDto;
import com.mqz.domin.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 11:12:56
 */
public interface MenuService extends IService<Menu> {
    List<String> getPermissions(Long id);
    List<Menu> getMenus(Long id);

    ResponseResult getAllMenu(MenuListDto menuListDto);

    ResponseResult addMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    ResponseResult getOneMenu(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult treeSelect();

    ResponseResult roleMenuTreeSelect(Long id);
}

