package com.imooc.seckill.service;

import com.imooc.seckill.domain.MiaoshaUser;
import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods){
//        减库存下订单
        goodsService.reduceStock(goods);
        return orderService.createOrder(user,goods);
    }
}
