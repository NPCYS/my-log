package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.entity.User;
import com.mqz.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
    @GetMapping("/getInfo")
    public ResponseResult getinfo(){
        return loginService.getinfo();
    }
    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        return loginService.getRouters();
    }
}
