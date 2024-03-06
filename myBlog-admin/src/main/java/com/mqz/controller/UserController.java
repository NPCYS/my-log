package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.UserListDto;
import com.mqz.domin.dto.UserStatusDto;
import com.mqz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/system")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto){
        return userService.getUserList(pageNum,pageSize,userListDto);
    }
    @PutMapping("/user/changeStatus")
    public ResponseResult changeStatus(@RequestBody UserStatusDto userStatusDto){
        return userService.changeStatus(userStatusDto);
    }
}
