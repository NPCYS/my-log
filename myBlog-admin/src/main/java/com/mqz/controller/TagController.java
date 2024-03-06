package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.TagListDto;
import com.mqz.domin.vo.TagVo;
import com.mqz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getUpdateTag(@PathVariable Long id){
        return tagService.getUpdateTag(id);
    }
    @PutMapping()
    public ResponseResult updateTag(@RequestBody TagVo tagVo){
        return tagService.updateTag(tagVo);
    }
    @PostMapping()
    public ResponseResult addTag(@RequestBody TagVo tagVo){
        return tagService.addTag(tagVo);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult deleteTags(@PathVariable String ids){
        return tagService.deleteTags(ids);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
