package com.mqz.service.impl;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.LoginUser;
import com.mqz.domin.entity.User;
import com.mqz.domin.vo.BlogUserLoginVo;
import com.mqz.domin.vo.UserInfoVo;
import com.mqz.service.BlogLoginService;
import com.mqz.utils.BeanCopyUtils;
import com.mqz.utils.JwtUtil;
import com.mqz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import static com.mqz.constants.SystemConstants.LOGIN_REDIS_KEY_PREFIX;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断验证是否通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或者密码错误！");
        }
        //获取用户id来生成token
        LoginUser loginUser =(LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String userIdStr = userId.toString();
        String jwt = JwtUtil.createJWT(userId.toString());
        //将用户的id和用户的信息保存到redis中去
        redisCache.setCacheObject(LOGIN_REDIS_KEY_PREFIX+userIdStr,loginUser);
        //把token和用户部分信息封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }
    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中保存的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser) authentication.getPrincipal();
        //解析id
        Long id = loginUser.getUser().getId();
        String userIdStr = id.toString();
        //删除redis中的用户信息
        redisCache.deleteObject(LOGIN_REDIS_KEY_PREFIX+userIdStr);
        return ResponseResult.okResult(200,"退出成功");
    }
}
