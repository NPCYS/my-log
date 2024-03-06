package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.AddArticleDto;
import com.mqz.domin.dto.ArticleListDto;
import com.mqz.domin.entity.Article;
import com.mqz.domin.entity.ArticleTag;
import com.mqz.domin.entity.Category;
import com.mqz.domin.vo.ArticleDetailVo;
import com.mqz.domin.vo.ArticleVo;
import com.mqz.domin.vo.HotArticleVo;
import com.mqz.domin.vo.PageVo;
import com.mqz.exception.SystemException;
import com.mqz.mapper.ArticleMapper;
import com.mqz.mapper.ArticleTagMapper;
import com.mqz.service.ArticleService;
import com.mqz.service.ArticleTagService;
import com.mqz.service.CategoryService;
import com.mqz.service.TagService;
import com.mqz.utils.BeanCopyUtils;
import com.mqz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mqz.constants.SystemConstants.ARTICLE_STATUS_NORMAL;
import static com.mqz.constants.SystemConstants.PageView_REDIS_KEY_PREFIX;
import static com.mqz.enums.AppHttpCodeEnum.ARTICLE_ID_IS_EMPTY;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleMapper articleMapper;
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
        for (HotArticleVo hotArticleVo : hotArticleVos) {
            hotArticleVo.setViewCount(getViewCount(hotArticleVo.getId()));
        }
        return ResponseResult.okResult(hotArticleVos);
    }

    private Long getViewCount(Long id) {
        String viewCountStr = redisCache.getCacheObject(PageView_REDIS_KEY_PREFIX + id.toString());
        return Long.valueOf(viewCountStr);
    }

    @Override
    public ResponseResult articleList(Long pageNum, Long pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);
        articleLambdaQueryWrapper.eq(categoryId!=0,Article::getCategoryId,categoryId);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        Page<Article> page = this.page(articlePage, articleLambdaQueryWrapper);
        List<Article> source = page.getRecords();
        //给Name赋值
        source = source.stream().map((Function<Article, Article>) article -> {
            article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
            return article;
        }).collect(Collectors.toList());
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(source, ArticleVo.class);
        for (ArticleVo articleVo : articleVos) {
            articleVo.setViewCount(getViewCount(articleVo.getId()));
        }
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
        articleDetailVo.setViewCount(getViewCount(articleDetailVo.getId()));
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        String key=PageView_REDIS_KEY_PREFIX+id.toString();
        String viewCount = redisCache.getCacheObject(key);
        Long aLong = Long.valueOf(viewCount);
        aLong+=1;
        viewCount = aLong.toString();
        redisCache.setCacheObject(key,viewCount);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto article) {
        //保存博文
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        saveOrUpdate(article1);
        //保存标签信息
        List<Long> tags = article.getTags();
        if (article1.getId()==null){
            throw new SystemException(ARTICLE_ID_IS_EMPTY);
        }
        List<ArticleTag> collect = tags.stream().map(tag -> new ArticleTag(article1.getId(), tag)).collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
        articleLambdaQueryWrapper.like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        Page<Article> page = page(articlePage, articleLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getOneArticle(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tagIds = list.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        AddArticleDto addArticleDto = BeanCopyUtils.copyBean(article, AddArticleDto.class);
        addArticleDto.setTags(tagIds);
        return ResponseResult.okResult(addArticleDto);
    }
    @Transactional
    @Override
    public ResponseResult updateArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        updateById(article);
        List<Long> tags = addArticleDto.getTags();
        List<ArticleTag> articleTags = tags.stream().map(tag -> new ArticleTag(article.getId(), tag)).collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(String id) {
        articleMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}
