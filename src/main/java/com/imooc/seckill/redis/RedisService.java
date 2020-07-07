package com.imooc.seckill.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.imooc.seckill.domain.MiaoshaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Repository
@Service
public class RedisService {

    private static JedisCluster jedis;
    static {
        // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort("49.234.206.197", 6379));
        hostAndPortsSet.add(new HostAndPort("49.234.206.197", 7000));
        hostAndPortsSet.add(new HostAndPort("49.234.216.248", 6379));
        hostAndPortsSet.add(new HostAndPort("49.234.216.248", 7000));
        hostAndPortsSet.add(new HostAndPort("106.54.86.131", 6379));
        hostAndPortsSet.add(new HostAndPort("106.54.86.131", 7000));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
//        jedis = new JedisCluster(hostAndPortsSet, jedisPoolConfig);
        jedis= new JedisCluster(hostAndPortsSet,0,5000,5,"19980504Jcy", jedisPoolConfig);

    }

    public <T> T get(KeyPrefix prefix,String key,Class<T> data){
        System.out.println("@RedisService-REDIES-GET!");
//        JedisCluster jedis=null;
        //在JedisPool里面取得Jedis
        try {
//            jedis=jedisPool.getJedisCluster();
            //生成真正的key  className+":"+prefix;  BasePrefix:id1
            String realKey=prefix.getPrefix()+key;
            System.out.println("@RedisService-get-realKey:"+realKey);
            //System.out.println("jedis:"+jedis);
            String sval=jedis.get(realKey);

            System.out.println("@RedisService-getvalue:"+sval);
            //将String转换为Bean入后传出
            T t=stringToBean(sval,data);
            return t;
        }finally {
//           returnToPool(jedis);
        }
    }


    /**
     * 移除对象,删除
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix,String key){
//        JedisCluster jedis=null;
        try {
//            jedis=jedisPool.getJedisCluster();
            String realKey=prefix.getPrefix()+key;
            long ret=jedis.del(realKey);
            return ret>0;//删除成功，返回大于0
            //return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置单个、多个对象
     * @param prefix
     * @param key
     * @param value
     * @return
     */						//MiaoshaUserKey.token, token, user
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        System.out.println("@RedisService-REDIES-SET!");
//        JedisCluster jedis=null;
        try {//在JedisPool里面取得Jedis
//            jedis=jedisPool.getJedisCluster();
            String realKey=prefix.getPrefix()+key;
            System.out.println("@RedisService-key:"+key);
            System.out.println("@RedisService-getPrefix:"+prefix.getPrefix());
            System.out.println("set-realKey:"+realKey);
            String s=beanToString(value);//将T类型转换为String类型，json类型？？
            //System.out.println("s:"+s);
            if(s==null||s.length()<=0) {
                return false;
            }
            int seconds=prefix.expireSeconds();
            if(seconds<=0) {//有效期：代表不过期，这样才去设置
                jedis.set(realKey, s);
                //System.out.println("1");
            }else {//没有设置过期时间，即没有设置有效期，那么自己设置。
                jedis.setex(realKey, seconds,s);
                //System.out.println("2");
            }
            return true;
        }finally {
           returnToPool(jedis);
            //System.out.println("3");
        }
    }
    /**
     * 减少值
     * @param prefix
     * @param key
     * @return
     */
    public <T> Long decr(KeyPrefix prefix,String key){
//        JedisCluster jedis=null;
        try {
//            jedis=jedisPool.getJedisCluster();
            String realKey=prefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 增加值
     * @param prefix
     * @param key
     * @return
     */
    public <T> Long incr(KeyPrefix prefix,String key){
//        JedisCluster jedis=null;
        try {
//            jedis=jedisPool.getJedisCluster();
            String realKey=prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
           returnToPool(jedis);
        }
    }
    /**
     * 检查key是否存在
     * @param prefix
     * @param key
     * @return
     */
    public <T> boolean exitsKey(KeyPrefix prefix,String key){
//        JedisCluster jedis=null;
        try {
//            jedis=jedisPool.getJedisCluster();
            String realKey=prefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 将字符串转换为Bean对象
     *
     * parseInt()返回的是基本类型int 而valueOf()返回的是包装类Integer
     * Integer是可以使用对象方法的  而int类型就不能和Object类型进行互相转换 。
     * int a=Integer.parseInt(s);
     Integer b=Integer.valueOf(s);
     */
    public static <T> T stringToBean(String s,Class<T> clazz) {
        if(s==null||s.length()==0||clazz==null) {
            return null;
        }
        if(clazz==int.class||clazz==Integer.class) {
            return ((T) Integer.valueOf(s));
        }else if(clazz==String.class) {
            return (T) s;
        }else if(clazz==long.class||clazz==Long.class) {
            return (T) Long.valueOf(s);
        }else {
            JSONObject json= JSON.parseObject(s);
            return JSON.toJavaObject(json, clazz);
        }
    }
    /**
     * 将Bean对象转换为字符串类型
     * @param <T>
     */
    public static <T> String beanToString(T value) {
        //如果是null
        if(value==null) return null;
        //如果不是null
        Class<?> clazz=value.getClass();
        if(clazz==int.class||clazz==Integer.class) {
            return ""+value;
        }else if(clazz==String.class) {
            return ""+value;
        }else if(clazz==long.class||clazz==Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }
    private void returnToPool(JedisCluster jedis) {
//       if(jedis!=null) {
//           try {
//               jedis.close();
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
//        }
    }
    public <T> boolean set(String key,T value){
//        JedisCluster jedis=null;
        //在JedisPool里面取得Jedis
        try {
//            jedis=jedisPool.getJedisCluster();
            //将T类型转换为String类型
            String s=beanToString(value);
            if(s==null) {
                return false;
            }
            jedis.set(key, s);
            return true;
        }finally {
           returnToPool(jedis);
        }
    }
    public <T> T get(String key,Class<T> data){
//        JedisCluster jedis=null;
        //在JedisPool里面取得Jedis
        try {
//            jedis=jedisPool.getJedisCluster();
            System.out.println("jedis:"+jedis);
            String sval=jedis.get(key);
            System.out.println("sval:"+sval);
            //将String转换为Bean入后传出
            T t = stringToBean(sval, data);
            return t;
        }finally {
           returnToPool(jedis);

        }
    }

}
