package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.CategoryListDto;
import com.mqz.domin.entity.Article;
import com.mqz.domin.entity.Category;
import com.mqz.domin.vo.CategoryVo;
import com.mqz.domin.vo.ExcelCategoryVo;
import com.mqz.domin.vo.PageVo;
import com.mqz.mapper.CategoryMapper;
import com.mqz.service.ArticleService;
import com.mqz.service.CategoryService;
import com.mqz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-04-20 21:26:16
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
        @Autowired
        private ArticleService articleService;
        @Autowired
        private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        //先查询通过的文章来获取他们的分类Id
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(articleLambdaQueryWrapper);
        Set<Long> categoryId = list.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        //再通过查到的分类Id查询存在文章的分类信息
        List<Category> categories = listByIds(categoryId);
        //获得那些没有被封的分类
        categories=categories.stream()
                .filter(category -> Long.valueOf(category.getStatus())==SystemConstants.CATEGORY_STATUS_NORMAL)
                        .collect(Collectors.toList());
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> list = list(categoryLambdaQueryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult CategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(StringUtils.hasText(categoryListDto.getName()),Category::getName,categoryListDto.getName());
        categoryLambdaQueryWrapper.eq(StringUtils.hasText(categoryListDto.getStatus()),Category::getStatus,categoryListDto.getStatus());
        Page<Category> page = page(categoryPage, categoryLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getOneCategory(Long id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);

    }

    @Override
    public ResponseResult updateCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(List<Long> ids) {
        categoryMapper.deleteBatchIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public List<ExcelCategoryVo> listExcelCategory() {
        List<Category> list = list();
        return BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
    }
}
