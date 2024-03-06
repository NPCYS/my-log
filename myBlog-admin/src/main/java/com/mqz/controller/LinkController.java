package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.LinkListDto;
import com.mqz.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, LinkListDto linkListDto){
        return linkService.getLinkList(pageNum,pageSize,linkListDto);
    }
}
