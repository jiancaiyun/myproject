package com.imooc.seckill.controller;

import com.imooc.seckill.domain.MiaoshaUser;
import com.imooc.seckill.redis.GoodsKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.MiaoshaUserService;
import com.imooc.seckill.util.CookieUtil;
import com.imooc.seckill.vo.GoodsDetailVo;
import com.imooc.seckill.vo.GoodsVo;
import javafx.application.Application;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.thymeleaf.spring4.context.SpringWebContext;
//import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.springframework.context.ApplicationContext;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

//import org.thymeleaf.spring4.view.ThymeleafViewResolver;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;

//import org.thymeleaf.spring5.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/goods")
@Controller
public class GoodsController {

    @Autowired
    GoodsService goodsService;
    @Autowired
    MiaoshaUser miaoshaUser;
    @Autowired
    RedisService redisService;
    //注入渲染
    @Autowired
    public ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @RequestMapping(value="/to_list",produces = "text/html")
    @ResponseBody
    public String list(Model model, MiaoshaUser user, HttpServletResponse response, HttpServletRequest request){
        //        取缓存
//        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
        String html=null;
        String token = CookieUtil.readLoginToken(request);
        user = miaoshaUserService.getByToken(token, response);
        model.addAttribute("user", user);
        //查询商品
        List<GoodsVo> goodsList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsList);
//        return "goods_list";

        SpringWebContext ctx = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
//        手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value="/detail/{goodsId}")
    public Result<GoodsDetailVo> toDetail_staticPage(Model model,MiaoshaUser user, HttpServletRequest request,HttpServletResponse response, @PathVariable("goodsId")long goodsId){
        System.out.println("页面静态化/detail/{goodsId}");
        String token = CookieUtil.readLoginToken(request);
        user = miaoshaUserService.getByToken(token, response);
        model.addAttribute("user",user);
        GoodsVo goodsVo=goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        //既然是秒杀，还要传入秒杀开始时间，结束时间等信息
        long start=goodsVo.getStartDate().getTime();
        long end=goodsVo.getEndDate().getTime();
        long now=System.currentTimeMillis();
        //秒杀状态量
        int status=0;
        //开始时间倒计时
        int remailSeconds=0;
        //查看当前秒杀状态
        if(now<start) {//秒杀还未开始，--->倒计时
            status=0;
            remailSeconds=(int) ((start-now)/1000);  //毫秒转为秒
        }else if(now>end){ //秒杀已经结束
            status=2;
            remailSeconds=-1;  //毫秒转为秒
        }else {//秒杀正在进行
            status=1;
            remailSeconds=0;  //毫秒转为秒
        }
        model.addAttribute("status", status);
        model.addAttribute("remailSeconds", remailSeconds);
        GoodsDetailVo gdVo = new GoodsDetailVo();
        gdVo.setGoodsVo(goodsVo);
        gdVo.setStatus(status);
        gdVo.setRemailSecond(remailSeconds);
        gdVo.setMiaoshaUser(user);
        return Result.success(gdVo);

    }

    @RequestMapping(value = "to_detail/{goodsId}",produces = "text/html")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId,HttpServletResponse response, HttpServletRequest request){
//        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
//        取缓存
//
        String html = null;
        String token = CookieUtil.readLoginToken(request);
        user = miaoshaUserService.getByToken(token, response);
        model.addAttribute("user", user);
        //根据Id查询商品
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long nowTime = System.currentTimeMillis();
        int remainSeconds = 0;
        int miaoshaStatus = 0;
        if(nowTime < startAt){
            miaoshaStatus = 0;
            remainSeconds =(int) (startAt - nowTime)/1000;
        }
        if(nowTime > startAt && nowTime < endAt){
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        if(nowTime > endAt){
            miaoshaStatus = 2;
            remainSeconds = -1;
        }


        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        return "goods_list";

        SpringWebContext ctx = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
//        手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
            System.out.println("TEST REDIS");
        }
        return html;
    }
}
