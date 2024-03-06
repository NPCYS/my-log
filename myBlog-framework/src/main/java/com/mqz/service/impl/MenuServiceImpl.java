package com.mqz.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.MenuListDto;
import com.mqz.domin.entity.Menu;
import com.mqz.domin.vo.MenuReturnVo;
import com.mqz.enums.AppHttpCodeEnum;
import com.mqz.mapper.MenuMapper;
import com.mqz.service.MenuService;
import com.mqz.utils.CopyBeanUtil;
import com.mqz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mqz.constants.SystemConstants.TREE_SELECT;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 11:12:56
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public List<String> getPermissions(Long id) {
        return menuMapper.getPermissions(id);
    }

    @Override
    public List<Menu> getMenus(Long id) {
        return menuMapper.getMenus(id);
    }

    @Override
    public ResponseResult getAllMenu(MenuListDto menuListDto) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.like(StringUtils.hasText(menuListDto.getMenuName()), Menu::getMenuName, menuListDto.getMenuName());
        menuLambdaQueryWrapper.like(StringUtils.hasText(menuListDto.getStatus()), Menu::getStatus, menuListDto.getStatus());
        menuLambdaQueryWrapper.orderByAsc(Menu::getId);
        menuLambdaQueryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> list = list(menuLambdaQueryWrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId, id);
        int count = count(menuLambdaQueryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MENU_CHILDREN_IS_NOT_EMPTY);
        } else {
            menuMapper.deleteById(id);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getOneMenu(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeSelect() {
        String cacheObject = redisCache.getCacheObject(TREE_SELECT);
        if (cacheObject != null) {
            List<MenuReturnVo> menuReturnVos = (List<MenuReturnVo>) JSON.parse(cacheObject);
            return ResponseResult.okResult(menuReturnVos);
        } else {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.eq(Menu::getParentId, SystemConstants.BACKGROUND_PARENTID);
            menuLambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> list = list(menuLambdaQueryWrapper);
            //找到了父节点
            List<MenuReturnVo> menuReturnVos = CreateMenuReturnVos(list);
            //找到父节点的子节点
            for (MenuReturnVo menuReturnVo : menuReturnVos) {
                LambdaQueryWrapper<Menu> menuLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                menuLambdaQueryWrapper1.eq(Menu::getParentId, menuReturnVo.getId());
                menuLambdaQueryWrapper1.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
                List<Menu> list1 = list(menuLambdaQueryWrapper1);
                List<MenuReturnVo> menuReturnVos1 = CreateMenuReturnVos(list1);
                for (MenuReturnVo returnVo : menuReturnVos1) {
                    LambdaQueryWrapper<Menu> menuLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                    menuLambdaQueryWrapper2.eq(Menu::getParentId, returnVo.getId());
                    menuLambdaQueryWrapper2.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
                    List<Menu> list2 = list(menuLambdaQueryWrapper2);
                    List<MenuReturnVo> menuReturnVos2 = CreateMenuReturnVos(list2);
                    returnVo.setChildren(menuReturnVos2);
                }
                menuReturnVo.setChildren(menuReturnVos1);
            }
            redisCache.setCacheObject(TREE_SELECT, JSON.toJSONString(menuReturnVos), 30, TimeUnit.MINUTES);
            return ResponseResult.okResult(menuReturnVos);
        }

    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        List<Menu> menus = new ArrayList<>();
        if (id == 1) {
            return this.treeSelect();
        }
        menus = menuMapper.getMenus(id);
        List<Menu> parentMenus = new ArrayList<>();
        List<MenuReturnVo> menuReturnVos = null;
        for (Menu menu : menus) {
            if (menu.getParentId() == 0) {
                parentMenus.add(menu);
            }
            //找到父节点
            menuReturnVos = CreateMenuReturnVos(parentMenus);
            //子节点
            ArrayList<MenuReturnVo> menuReturnVos1 = new ArrayList<>();
            //子子节点
            ArrayList<MenuReturnVo> menuReturnVos2 = new ArrayList<>();
            for (MenuReturnVo menuReturnVo : menuReturnVos) {
                for (Menu menu1 : menus) {
                    if (menu1.getParentId() == menuReturnVo.getId()) {
                        MenuReturnVo menuReturnVo2 = new MenuReturnVo();
                        CopyBeanUtil.copyProperties(menu1, menuReturnVo2);
                        menuReturnVos1.add(menuReturnVo2);
                    }
                }
                menuReturnVo.setChildren(menuReturnVos1);
            }
            for (MenuReturnVo menuReturnVo3 : menuReturnVos1) {
                for (Menu menu2 : menus) {
                    if (menu2.getParentId() == menuReturnVo3.getId()) {
                        MenuReturnVo menuReturnVo2 = new MenuReturnVo();
                        CopyBeanUtil.copyProperties(menu2, menuReturnVo2);
                        menuReturnVos2.add(menuReturnVo2);
                    }
                }
                menuReturnVo3.setChildren(menuReturnVos2);
            }
        }
        return ResponseResult.okResult(menuReturnVos);
    }

    public List<MenuReturnVo> CreateMenuReturnVos(List<Menu> list) {
        List<MenuReturnVo> menuReturnVos = new ArrayList<>();
        for (Menu menu : list) {
            MenuReturnVo menuReturnVo = new MenuReturnVo();
            CopyBeanUtil.copyProperties(menu, menuReturnVo);
            menuReturnVos.add(menuReturnVo);
        }
        return menuReturnVos;
    }
}
