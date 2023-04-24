package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult<Article> hotArlticeList();

    ResponseResult articleList(Long pageNum, Long pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);
}
