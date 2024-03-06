package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TagService tagService;
    @GetMapping("/tag/{id}")
        public ResponseResult test(@PathVariable("id") Long id){
        return tagService.getTag(id);
        }
}
