package com.mqz.controller;

import com.mqz.annotation.SystemLog;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.User;
import com.mqz.domin.vo.UserInfoVo;
import com.mqz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult update(@RequestBody UserInfoVo userInfoVo){
        return userService.updateUserInfo(userInfoVo);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
