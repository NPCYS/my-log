package com.mqz.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.entity.Article;
import com.mqz.service.ArticleService;
import com.mqz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mqz.constants.SystemConstants.PageView_REDIS_KEY_PREFIX;

@Component
public class redisToMysqlJob {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCache redisCache;

    //0/10 * * * * ?每10秒运行一次
    //每10分钟运行一次
    @Scheduled(cron = "* 0/10 * * * ?")
    public void updateViewCount(){
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(articleLambdaQueryWrapper);
        for (Article article : list) {
            Long id = article.getId();
            String viewCountStr = redisCache.getCacheObject(PageView_REDIS_KEY_PREFIX + id.toString());
            Long viewCount = Long.valueOf(viewCountStr);
            article.setViewCount(viewCount);
            articleService.updateById(article);
        }
    }
}
