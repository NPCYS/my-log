package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.User;
import com.mqz.exception.SystemException;
import com.mqz.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mqz.enums.AppHttpCodeEnum.REQUIRE_USERNAME;

@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
             throw new SystemException(REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);

    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }

}
