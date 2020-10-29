package com.offcn.user.controller;

import com.mysql.cj.util.TimeUtil;
import com.offcn.common.response.AppResponse;
import com.offcn.user.componet.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.serivce.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户注册登录模块")
@RestController
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @ApiOperation("发送短信的操作")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "phoneNum",value = "手机号",required = true)
    })
    @PostMapping("/sendCode")
    public AppResponse<Object> sendCode(String phoneNum){
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);

        redisTemplate.opsForValue().set(phoneNum, code,60*5, TimeUnit.SECONDS);

        Map<String,String> params = new HashMap<>();
        params.put("mobile", phoneNum);
        params.put("param", "code:"+code);
        params.put("tpl_id", "TP1711063");
        String result = smsTemplate.sendCode(params);
        if (result.equals("") || result.equals("fail")){
            return AppResponse.fail("短信发送失败");
        }else {
            return AppResponse.ok(result);
        }

    }


    @ApiOperation("注册的操作")
    @PostMapping("/regist")
    public AppResponse<Object> userRegist(UserRegistVo vo){
        String code = redisTemplate.opsForValue().get(vo.getLoginacct());
        if (code != null && !code.equals("")){
            boolean b = code.equalsIgnoreCase(vo.getCode());
            if (b){
                TMember tm = new TMember();
                try {
                    //复制属性类
                    BeanUtils.copyProperties(vo, tm);
                    userService.registerUser(tm);
                    return AppResponse.ok("注册成功");
                }catch (Exception e){
                    e.printStackTrace();
                    return AppResponse.fail("注册失败");
                }

            }else {
                return AppResponse.fail("验证码错误");
            }
        }else {
            return AppResponse.fail("验证码错误，重新获取");
        }
    }

    @ApiOperation("登录的操作")
    @PostMapping("/login")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username",value = "用户手机号",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",required = true)

    })
    public AppResponse<UserRespVo> login(String username, String password){
        TMember member = userService.login(username, password);
        if (member == null){
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户登录失败");
            return fail;
        }
        //生成令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member, vo);
        vo.setAccessToken(token);
        redisTemplate.opsForValue().set(token, member.getId()+"",2,TimeUnit.HOURS);
        return AppResponse.ok(vo);
    }

    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tmember = userService.findTmemberById(id);
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(tmember, vo);
        return AppResponse.ok(vo);
    }
}
