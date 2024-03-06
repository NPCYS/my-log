package com.mqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mqz.domin.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-04 11:12:13
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Long> getRole(Long userId);

}
