package com.mqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-04-21 19:38:51
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}

