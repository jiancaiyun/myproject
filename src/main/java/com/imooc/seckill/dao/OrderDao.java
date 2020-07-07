package com.imooc.seckill.dao;

import com.imooc.seckill.domain.MiaoshaOrder;
import com.imooc.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

    @Select(" select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long usrid,@Param("goodsId") long goodsId);

//    @Insert("insert into order_info (user_id,goods_id,goods_name,goods_count,goods_price,order_channel,order_status,create_date) values "
//            + "(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{orderStatus},#{createDate})")
//    @SelectKey(keyColumn="id",keyProperty="id",resultType=long.class,before=false,statement="select last_insert_id()")
//    public long insert(OrderInfo orderInfo);//使用注解获取返回值，返回上一次插入的id
@Insert("insert into order_info (user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values "
        + "(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{orderStatus},#{createDate})")
@SelectKey(keyColumn="id",keyProperty="id",resultType=long.class,before=false,statement="select last_insert_id() ")
public long insert(OrderInfo orderInfo);//使用注解获取返回值，返回上一次插入的id


//    @Insert("insert into miaosha_order (user_id,goods_id,order_id) values (#{userId},#{goodsId},#{orderId})")
//    public void insertMiaoshaOrder(MiaoshaOrder miaoshaorder);
@Insert("insert into miaosha_order (user_id,goods_id,order_id) values (#{userId},#{goodsId},#{orderId})")
public void insertMiaoshaOrder(MiaoshaOrder miaoshaorder);
}
