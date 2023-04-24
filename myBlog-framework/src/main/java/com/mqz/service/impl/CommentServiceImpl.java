package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.constants.SystemConstants;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Comment;
import com.mqz.domin.vo.PageVo;
import com.mqz.domin.vo.RootCommentVo;
import com.mqz.exception.SystemException;
import com.mqz.mapper.CommentMapper;
import com.mqz.service.CommentService;
import com.mqz.service.UserService;
import com.mqz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mqz.constants.SystemConstants.COMMENT_ROOT_ID;
import static com.mqz.constants.SystemConstants.COMMENT_TO_COMMENT_USERID;
import static com.mqz.enums.AppHttpCodeEnum.REQUIRE_COMMENT;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-23 14:12:37
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    /**
     * 获取评论列表（包括子评论）
     *
     * @param commentType
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询根评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //找到该文章/友链的评论
        commentLambdaQueryWrapper.eq(Comment::getType,commentType);
        commentLambdaQueryWrapper.eq(SystemConstants.COMMENT_TYPE.equals(commentType),Comment::getArticleId,articleId);
        //找到根评论
        commentLambdaQueryWrapper.eq(Comment::getRootId,COMMENT_ROOT_ID);
        //根据时间排序
        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        Page<Comment> page = page(commentPage, commentLambdaQueryWrapper);
        List<Comment> commentList = page.getRecords();
        List<RootCommentVo> collect = commentList.stream()
                .map(comment -> BeanCopyUtils.copyBean(comment, RootCommentVo.class))
                .collect(Collectors.toList());
        //给UserName和toCommentUserName赋值(查的都是昵称)
        List<RootCommentVo> rootCommentVoList = collect.stream()
                .map((Function<RootCommentVo, RootCommentVo>) rootCommentVo -> {
                    Long userId = rootCommentVo.getCreateBy();
                    String userName = userService.getById(userId).getNickName();
                    if (StringUtils.hasText(userName)){
                        rootCommentVo.setUsername(userName);
                    }
                    if (rootCommentVo.getToCommentUserId()!=COMMENT_TO_COMMENT_USERID){
                        Long toCommentUserId = rootCommentVo.getToCommentUserId();
                        String userName1 = userService.getById(toCommentUserId).getNickName();
                        if (StringUtils.hasText(userName1)){
                            rootCommentVo.setToCommentUserName(userName1);
                        }
                    }
                    return rootCommentVo;
                }).collect(Collectors.toList());
        //查询子评论
        for (RootCommentVo rootCommentVo : rootCommentVoList) {
            List<RootCommentVo> children = GetChildren(rootCommentVo.getId());
            if (!children.isEmpty()){
                rootCommentVo.setChildren(children);
            }
        }
        long total = page.getTotal();
        PageVo pageVo = new PageVo(rootCommentVoList, total);
        //分页查询并返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult comment(Comment comment) {
        //获取作者（用自动填充了）
        String content = comment.getContent();
        if (!StringUtils.hasText(content)){
            throw new SystemException(REQUIRE_COMMENT);
        }
        this.save(comment);
        //保存评论
        return ResponseResult.okResult();
    }
    /**
     * 查询子评论
     * @param id 根评论的id
     * @return
     */
    private List<RootCommentVo> GetChildren(Long id) {
        LambdaQueryWrapper<Comment> rootCommentVoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rootCommentVoLambdaQueryWrapper.eq(Comment::getRootId,id);
        List<Comment> list = list(rootCommentVoLambdaQueryWrapper);
        List<RootCommentVo> childrenCommentVoList = BeanCopyUtils.copyBeanList(list, RootCommentVo.class);
        childrenCommentVoList=childrenCommentVoList.stream()
                .map((Function<RootCommentVo, RootCommentVo>) rootCommentVo -> {
                    Long userId = rootCommentVo.getCreateBy();
                    String userName = userService.getById(userId).getNickName();
                    if (StringUtils.hasText(userName)) {
                        rootCommentVo.setUsername(userName);
                    }
                    if (rootCommentVo.getToCommentUserId() != COMMENT_TO_COMMENT_USERID) {
                        Long toCommentUserId = rootCommentVo.getToCommentUserId();
                        String userName1 = userService.getById(toCommentUserId).getNickName();
                        if (StringUtils.hasText(userName1)) {
                            rootCommentVo.setToCommentUserName(userName1);
                        }
                    }
                    return rootCommentVo;
                }).collect(Collectors.toList());
        return childrenCommentVoList;
    }

}
