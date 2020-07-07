package com.imooc.seckill.service;

import com.imooc.seckill.dao.MiaoshaUserDao;
import com.imooc.seckill.domain.MiaoshaUser;
import com.imooc.seckill.exception.GlobalException;
import com.imooc.seckill.redis.MiaoshaUserKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.util.CookieUtil;
import com.imooc.seckill.util.MD5Util;
import com.imooc.seckill.util.UUIDUtil;
import com.imooc.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    public static final String COOKIE1_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){
        //1.取缓存，先根据id取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id,MiaoshaUser.class);
        //缓存中可以取到
        if(user != null)
            return user;
        //缓存中取不到
        user = miaoshaUserDao.getById(id);
        //把从数据库中取到的user放入缓存中
        if(user != null){
            redisService.set(MiaoshaUserKey.getById, ""+id, user);
        }
        return user;
    }
    public boolean updatePassword(String token, long id, String passNew){
        //取user对象，查看是否存在
        MiaoshaUser user = getById(id);
        if(user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOTEXIST);
        //更新密码
        MiaoshaUser toUpdateuser = new MiaoshaUser();
        toUpdateuser.setId(id);
        toUpdateuser.setPwd(MD5Util.inputPassToDbPass(passNew, user.getSalt()));
        miaoshaUserDao.update(toUpdateuser);
        //更新数据库与缓存，一定保证数据一致性，修改token关联的对象以及id关联的对象
        redisService.delete(MiaoshaUserKey.getById, ""+ id);
        //不能直接删除token，删除之后就不能登录了
        user.setPwd(toUpdateuser.getPwd());
        System.out.println("set token to redis");
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    public MiaoshaUser getByToken(String token, HttpServletResponse response){
        if(StringUtils.isEmpty(token))
        return null;
        System.out.println("get token from redids");
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 再次请求时候，延长有效期
        // 重新设置缓存里面的值，使用之前cookie里面的token
        if(user!=null) {
           CookieUtil.writeLoginToken(response,token);
        }
        System.out.println("get token");
        System.out.println("@MiaoshaUserService-getByToken  user:"+user);
        return user;
    }

    public String loginString(HttpServletResponse response, LoginVo loginVo){
        if(loginVo == null){
            return CodeMsg.SERVER_ERROR.getMsg();
        }
        //经过了以此MD5的密码
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        //查询不到该手机号用户
        if(user == null){
            return CodeMsg.MOBILE_NOTEXIST.getMsg();
        }
        //手机号存在的情况，验证密码，获取数据库里面的密码与salt去验证
        String dbPass = user.getPwd();
        String dbSalt = user.getSalt();
        System.out.println("dbPass:" +dbPass+ "  dbSalt" +dbSalt);
        //验证密码计算二次MD5出来的pass是否与数据库一致
        String tmppass = MD5Util.formPassToDBPass(formPass, dbSalt);
        System.out.println("formPass:"+formPass);
        System.out.println("tmppass:"+tmppass);
        if(!tmppass.equals(dbPass)){
            return CodeMsg.PASSWORD_ERROR.getMsg();
        }
        //生成cookie
        String token = UUIDUtil.uuid();
       CookieUtil.writeLoginToken(response,token);
        redisService.set(MiaoshaUserKey.token, token, user);
        return token;
    }
    public CodeMsg login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo==null) {
            return CodeMsg.SERVER_ERROR;
        }
        //经过了依次MD5的密码
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user=getById(Long.parseLong(mobile));
        //查询不到该手机号的用户
        if(user==null) {
            return CodeMsg.MOBILE_NOTEXIST;
        }
        //手机号存在的情况，验证密码，获取数据库里面的密码与salt去验证
        //111111--->e5d22cfc746c7da8da84e0a996e0fffa
        String dbPass=user.getPwd();
        String dbSalt=user.getSalt();
        System.out.println("dbPass:"+dbPass+"   dbSalt:"+dbSalt);
        //验证密码，计算二次MD5出来的pass是否与数据库一致
        String tmppass=MD5Util.formPassToDBPass(formPass, dbSalt);
        System.out.println("formPass:"+formPass);
        System.out.println("tmppass:"+tmppass);
        if(!tmppass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }
        //生成cookie
        String token = UUIDUtil.uuid();
//      addCookie(user,token,response);
        CookieUtil.writeLoginToken(response,token);
        redisService.set(MiaoshaUserKey.token, token, user);
        return CodeMsg.SUCCESS;

    }

//    public void addCookie(MiaoshaUser user, String token, HttpServletResponse response){
//        //可以用老的token，不用每次都生成cookie
//        System.out.println("uuid" + token);
//        //将token写到cookie中，然后传给客户端
//        //此token对应的是哪一个用户，将我们的私人信息存放到第三方缓存中
//        //prefix：
//        redisService.set(MiaoshaUserKey.token, token, user);
//        Cookie cookie = new Cookie(COOKIE1_NAME_TOKEN, token);
//        //设置cookie的有效期，与session的有效期一致
//        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//        //设置网站的根目录
//        cookie.setPath("/");
//        System.out.println("set cookie");
//        //需要写到response中
//        response.addCookie(cookie);
//    }




}
