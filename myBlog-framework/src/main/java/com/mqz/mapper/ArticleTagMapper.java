package com.mqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mqz.domin.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-06 08:45:03
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
