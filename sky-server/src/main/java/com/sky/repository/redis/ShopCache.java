package com.sky.repository.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ShopCache {

    private static final String ketOfStatus = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    public void setStatus(Integer status){
        System.out.println(redisTemplate.getValueSerializer());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(ketOfStatus,status);
    }

    public Integer getStatus() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer shopStatus = (Integer) valueOperations.get(ketOfStatus);
        return shopStatus;
    }
}
