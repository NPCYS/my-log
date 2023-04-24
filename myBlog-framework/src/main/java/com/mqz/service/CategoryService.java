package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-04-20 21:26:16
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}

