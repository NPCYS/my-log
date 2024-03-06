package com.mqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mqz.domin.entity.Category;
import org.apache.ibatis.annotations.Mapper;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-20 21:28:00
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
