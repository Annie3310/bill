package me.wjy.bill.utils;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 王金义
 * @date 2021/8/12
 */
public class RedisUtil {
    private static RedisTemplate redisTemplate = new RedisTemplate();

    public static void setKey(String userId) {
//        redisTemplate.opsForHash().put();
    }
}
