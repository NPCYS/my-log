package com.mqz.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.entity.Article;
import com.mqz.service.ArticleService;
import com.mqz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.mqz.constants.SystemConstants.PageView_REDIS_KEY_PREFIX;

@Component
public class PageViewRunner implements CommandLineRunner {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(articleLambdaQueryWrapper);
        for (Article article : list) {
            redisCache.setCacheObject(PageView_REDIS_KEY_PREFIX+article.getId(),article.getViewCount());
        }
    }
}
