package com.mqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mqz.domin.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-04 09:05:11
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    void tombstone(Long tagId);

}
