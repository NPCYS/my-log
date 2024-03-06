package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.AddArticleDto;
import com.mqz.domin.dto.ArticleListDto;
import com.mqz.domin.entity.Article;
import com.mqz.service.ArticleService;
import org.aspectj.lang.annotation.DeclareError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.getArticleList(pageNum,pageSize,articleListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getOneArticle(@PathVariable Long id){
        return articleService.getOneArticle(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto addArticleDto){
        return articleService.updateArticle(addArticleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable String id){
        return articleService.deleteArticle(id);
    }

}
