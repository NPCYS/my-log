package com.mqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.domin.entity.ArticleTag;
import com.mqz.mapper.ArticleTagMapper;
import com.mqz.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-06 08:45:03
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
