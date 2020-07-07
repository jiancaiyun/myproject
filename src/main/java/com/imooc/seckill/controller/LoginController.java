package com.imooc.seckill.controller;

import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.MiaoshaUserService;
import com.imooc.seckill.service.UserService;
import com.imooc.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/login")
@Controller
public class LoginController {


    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;

    private static Logger log = LoggerFactory.getLogger(Logger.class);

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";//返回登录页面
    }

    //使用JSR303校验
    @RequestMapping("/do_login_test")
    @ResponseBody
    public Result<String> doLogintest(HttpServletResponse response, @Valid LoginVo loginVo) {
        String token = miaoshaUserService.loginString(response, loginVo);
        return Result.success(token);
    }

    @RequestMapping("/do_login")//作为异步操作
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {//0代表成功
        //log.info(loginVo.toString());
        //参数校验  使用了注解参数校验
//			String passInput=loginVo.getPassword();
//			String mobile=loginVo.getMobile();
//			if(StringUtils.isEmpty(passInput)) {
//				return Result.error(CodeMsg.PASSWORD_EMPTY);
//			}
//			if(StringUtils.isEmpty(mobile)) {
//				return Result.error(CodeMsg.MOBILE_EMPTY);
//			}
//			System.out.println("mobile："+mobile);
//			if(!ValidatorUtil.isMobile(mobile)) {//手机号验证不通过 false
//				return Result.error(CodeMsg.MOBILE_ERROR);
//			}
        //参数检验成功之后，登录
        CodeMsg cm = miaoshaUserService.login(response, loginVo);
        if (cm.getCode() == 0) {
            return Result.success(true);
        } else {
            return Result.error(cm);
        }


    }
}
