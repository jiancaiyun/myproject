package com.imooc.seckill.service;

import com.imooc.seckill.dao.GoodsDao;
import com.imooc.seckill.domain.Goods;
import com.imooc.seckill.domain.MiaoshaGoods;
import com.imooc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> getGoodsVoList(){return goodsDao.getGoodsVo();}
    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {
       MiaoshaGoods g = new MiaoshaGoods();
       g.setGoodsId(goods.getId());

        goodsDao.reduceStock(g);
    }
}
