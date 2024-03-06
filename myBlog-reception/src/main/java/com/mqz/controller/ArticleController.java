package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Article;
import com.mqz.service.ArticleService;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
//    @GetMapping("/list")
//    public List<Article> test(){
//        List<Article> list = articleService.list();
//        return list;
//    }
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        return articleService.hotArlticeList();
    }
    @GetMapping("/articleList")
    public ResponseResult articleList(Long pageNum,Long pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
        public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
        }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
                return articleService.updateViewCount(id);
    }
}
