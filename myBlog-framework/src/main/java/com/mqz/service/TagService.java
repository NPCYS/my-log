package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.TagListDto;
import com.mqz.domin.entity.Tag;
import com.mqz.domin.vo.TagVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 09:05:11
 */
public interface TagService extends IService<Tag> {

    ResponseResult getTag(Long id);

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult getUpdateTag(Long id);

    ResponseResult updateTag(TagVo tagVo);

    ResponseResult addTag(TagVo tagVo);

    ResponseResult deleteTags(String ids);

    void tombstone(Long tagId);

    ResponseResult listAllTag();
}

