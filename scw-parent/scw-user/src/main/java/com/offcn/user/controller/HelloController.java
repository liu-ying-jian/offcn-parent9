package com.offcn.user.controller;

import com.offcn.user.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "第一个swagger请求")
@RestController
public class HelloController {

    @ApiOperation("测试请求方法的数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "姓名",required = true)
    })
    @GetMapping("/hello")
    public String hello(String name){
        return "第一个请求";
    }

    @ApiOperation("测试添加用户的操作")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "姓名",required = true),
            @ApiImplicitParam(name = "id",value = "ID识别码",required = true)
    })
    @PostMapping("/user")
    public User createUser(String name,Integer id){
        return new User(id,name);
    }
}
