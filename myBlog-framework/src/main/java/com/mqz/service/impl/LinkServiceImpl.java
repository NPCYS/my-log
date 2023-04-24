package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Link;
import com.mqz.domin.vo.LinkVo;
import com.mqz.mapper.LinkMapper;
import com.mqz.service.LinkService;
import com.mqz.utils.BeanCopyUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

import static com.mqz.constants.SystemConstants.LINK_STATUS_NORMAL;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-04-21 19:38:51
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //检查所有审核通过的友链
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getStatus,LINK_STATUS_NORMAL);
        List<Link> list = list(linkLambdaQueryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);
        if (linkVos.isEmpty()){
            return ResponseResult.okResult("友链为空！");
        }
        return ResponseResult.okResult(linkVos);
    }
}
