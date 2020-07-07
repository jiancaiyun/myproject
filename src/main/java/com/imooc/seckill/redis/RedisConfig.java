package com.imooc.seckill.redis;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;


//@ConfigurationProperties(prefix = "spring.redis")
@PropertySource("application.properties")
@NoArgsConstructor
@Data
@Accessors(chain=true)
@Component
public class RedisConfig implements Serializable {
//    private String host;
//    private int port;
    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;

    private int poolMaxTotal;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int poolMaxldle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int poolMaxWait;
//    @Value("${spring.redis.commandTime}")
//    private int commandTimeout;

//    public static void main(String[] args) {
//        System.out.println(password);
//    }

}
