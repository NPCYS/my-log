package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.TagListDto;
import com.mqz.domin.entity.Tag;
import com.mqz.domin.vo.PageVo;
import com.mqz.domin.vo.TagVo;
import com.mqz.exception.SystemException;
import com.mqz.mapper.TagMapper;
import com.mqz.service.TagService;
import com.mqz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mqz.constants.SystemConstants.CONFIRM_DELETE;
import static com.mqz.constants.SystemConstants.TAG_STATUS_NORMAL;
import static com.mqz.enums.AppHttpCodeEnum.*;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 09:05:11
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = getById(id);
        return ResponseResult.okResult(tag);
    }
    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        Page<Tag> tagPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.like(StringUtils.hasText(tagListDto.getName()), Tag::getName,tagListDto.getName());
        tagLambdaQueryWrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = page(tagPage, tagLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getUpdateTag(Long id) {
        Tag tag = getById(id);
        if (tag==null){
            throw new SystemException(TAG_NOT_FOUND);
        }
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);

        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagVo tagVo) {
        Tag tag = getById(tagVo.getId());
        if (tag==null){
            throw new SystemException(TAG_NOT_FOUND);
        }
        tag.setName(tagVo.getName());
        tag.setRemark(tagVo.getRemark());
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addTag(TagVo tagVo) {
        if (!StringUtils.hasText(tagVo.getName())){
            throw new SystemException(TAG_ADD_NOT_NAME);
        }
        if (!StringUtils.hasText(tagVo.getRemark())){
            throw new SystemException(TAG_ADD_NOT_REMARK);
        }
        Tag tag = new Tag();
        tag.setName(tagVo.getName());
        tag.setRemark(tagVo.getRemark());
        this.save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTags(String ids) {
        String[] split = ids.split(",");
        List<Long> idsL = new ArrayList<>();
        for (String s : split) {
            Long id = Long.valueOf(s);
            idsL.add(id);
        }
        for (Long aLong : idsL) {
            tombstone(aLong);
        }
        return ResponseResult.okResult();
    }

    @Override
    public void tombstone(Long tagId) {
        tagMapper.tombstone(tagId);
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.eq(Tag::getDelFlag,TAG_STATUS_NORMAL);
        List<Tag> list = list(tagLambdaQueryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}
