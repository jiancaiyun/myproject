package com.imooc.seckill.controller;

import com.imooc.seckill.domain.MiaoshaOrder;
import com.imooc.seckill.domain.MiaoshaUser;
import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.MiaoshaService;
import com.imooc.seckill.service.MiaoshaUserService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.util.CookieUtil;
import com.imooc.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/miaosha")
@Controller
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    OrderService orderService;

//    @RequestMapping("/do_miaosha")
//    public String list (Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId, HttpServletRequest request, HttpServletResponse response){
//        String token = CookieUtil.readLoginToken(request);
//        user = miaoshaUserService.getByToken(token, response);
//        model.addAttribute("user",user);
//
//        if(user == null){
//            return "login";
//        }
////        判断库存
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock=goods.getStockCount();
//        if(stock<=0){
//            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER_ERROR.getMsg());
//            return "miaosha_fail";
//        }
////        判断是否已经秒杀dao
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
//        if(order != null){
//            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return "miaosha_fail";
//        }
//        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
//        model.addAttribute("orderinfo",orderInfo);
//        model.addAttribute("goods",goods);
//        return "order_detail";
//    }
@RequestMapping("/do_miaosha")//传入user对象啊，不然怎么取user的值，${user.nickname}
public String toList(Model model,MiaoshaUser user,@RequestParam("goodsId") Long goodsId,HttpServletRequest request, HttpServletResponse response) {
    String token = CookieUtil.readLoginToken(request);
       user = miaoshaUserService.getByToken(token, response);
    model.addAttribute("user", user);
    //如果用户为空，则返回至登录页面
    if(user==null){
        return "login";
    }
    GoodsVo goodsvo=goodsService.getGoodsVoByGoodsId(goodsId);
    //判断商品库存，库存大于0，才进行操作，多线程下会出错
    int  stockcount=goodsvo.getStockCount();
    if(stockcount<=0) {//失败			库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
        model.addAttribute("errorMessage", CodeMsg.MIAOSHA_OVER_ERROR);
        return "miaosha_fail";
    }
    //判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
    MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
    if(order!=null) {//重复下单
        model.addAttribute("errorMessage", CodeMsg.REPEATE_MIAOSHA);
        return "miaosha_fail";
    }
    //可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
    OrderInfo orderinfo=miaoshaService.miaosha(user,goodsvo);
    //如果秒杀成功，直接跳转到订单详情页上去。
    model.addAttribute("orderinfo", orderinfo);
    model.addAttribute("goods", goodsvo);
    return "order_detail";//返回页面login
}


}
