package com.mqz.filter;

import com.alibaba.fastjson.JSON;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.LoginUser;
import com.mqz.utils.JwtUtil;
import com.mqz.utils.RedisCache;
import com.mqz.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.mqz.constants.SystemConstants.*;
import static com.mqz.enums.AppHttpCodeEnum.NEED_LOGIN;
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = httpServletRequest.getHeader(HTTP_HEADER);
        if (!StringUtils.hasText(token)){
            //没有登录，放行让后面的处理去
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        Claims claims=null;
        try {
            claims= JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //如果解析异常就证明token过期了需要重新登录
            ResponseResult result = ResponseResult.errorResult(NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }
        //解析token获取其中的userId
        String userId = claims.getSubject();
        //从redis中获取用户信息
        String redisKey=ADMIN_LOGIN_REDIS_KEY_PREFIX+userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);

        if (Objects.isNull(loginUser)){
            //如果redis中没有信息也是需要重新登录的
            ResponseResult result = ResponseResult.errorResult(NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        emptyContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(emptyContext);
        //完成一切工作后，放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
