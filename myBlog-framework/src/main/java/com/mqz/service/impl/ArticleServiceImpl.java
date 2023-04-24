package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Article;
import com.mqz.domin.entity.Category;
import com.mqz.domin.vo.ArticleDetailVo;
import com.mqz.domin.vo.ArticleVo;
import com.mqz.domin.vo.HotArticleVo;
import com.mqz.domin.vo.PageVo;
import com.mqz.mapper.ArticleMapper;
import com.mqz.service.ArticleService;
import com.mqz.service.CategoryService;
import com.mqz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mqz.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseResult<Article> hotArlticeList() {
        //查询正式发布的文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        //按照浏览量降序排列返回
        articleLambdaQueryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = page(new Page<>(1, 10), articleLambdaQueryWrapper);
        //返回文章列表
        List<Article> records = page.getRecords();
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Long pageNum, Long pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);
        articleLambdaQueryWrapper.eq(categoryId!=null,Article::getCategoryId,categoryId);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        Page<Article> page = this.page(articlePage, articleLambdaQueryWrapper);
        List<Article> source = page.getRecords();
        //给Name赋值
        source = source.stream().map((Function<Article, Article>) article -> {
            article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
            return article;
        }).collect(Collectors.toList());
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(source, ArticleVo.class);
        PageVo pageVo = new PageVo(articleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //获取文章的详细信息
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        Article source = getById(id);
        Category byId = categoryService.getById(source.getCategoryId());
        if (byId!=null){
            source.setCategoryName(byId.getName());
        }
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(source, ArticleDetailVo.class);
        return ResponseResult.okResult(articleDetailVo);
    }
}
