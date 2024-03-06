package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.AddArticleDto;
import com.mqz.domin.dto.ArticleListDto;
import com.mqz.domin.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult<Article> hotArlticeList();

    ResponseResult articleList(Long pageNum, Long pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getOneArticle(Long id);

    ResponseResult updateArticle(AddArticleDto addArticleDto);

    ResponseResult deleteArticle(String id);
}
