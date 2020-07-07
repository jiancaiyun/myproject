package com.imooc;

import com.imooc.seckill.redis.RedisPoolFactory;
import com.imooc.seckill.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

/**
 * Hello world!
 *
 */
public class App
{
    @Autowired
    static RedisPoolFactory redisPoll;

    public static void main( String[] args )
    {
        JedisCluster jedis = redisPoll.getJedisCluster();
       String a= jedis.get("hello");
        System.out.println(a);
    }
}
