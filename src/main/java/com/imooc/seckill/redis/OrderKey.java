package com.imooc.seckill.redis;

public class OrderKey extends BasePrefix {

    public OrderKey(String prefix){ super(prefix);}
     public static OrderKey getMiaoshaOrderByUidAndGid=new OrderKey("ms_uidgid");
}
