package com.mqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mqz.domin.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-21 20:23:39
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
