package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.CategoryListDto;
import com.mqz.domin.entity.Category;
import com.mqz.domin.vo.ExcelCategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-04-20 21:26:16
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult CategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult addCategory(Category category);

    ResponseResult getOneCategory(Long id);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteCategory(List<Long> ids);

    List<ExcelCategoryVo> listExcelCategory();
}

