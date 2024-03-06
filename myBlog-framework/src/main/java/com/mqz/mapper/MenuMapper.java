package com.mqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mqz.domin.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-04 11:12:56
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
 List<String> getPermissions(Long userId);
 List<Menu> getMenusById(Long userId);
 List<Menu> getMenus(Long roleId);
}
