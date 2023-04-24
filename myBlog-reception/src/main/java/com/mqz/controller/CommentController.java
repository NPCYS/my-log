package com.mqz.controller;

import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Comment;
import com.mqz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult getCommentList(String commentType,Long articleId, Integer pageNum,Integer pageSize){
            return commentService.getCommentList(SystemConstants.COMMENT_TYPE,articleId,pageNum,pageSize);
    }
    @PostMapping()
    public ResponseResult comment(@RequestBody Comment comment){
        return commentService.comment(comment);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.getCommentList(SystemConstants.LINK_TYPE,null,pageNum,pageSize);
    }
}
