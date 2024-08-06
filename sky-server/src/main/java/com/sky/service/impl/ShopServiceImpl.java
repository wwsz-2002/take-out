package com.sky.service.impl;

import com.sky.repository.redis.ShopCache;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopCache shopCache;

    @Override
    public void setStatus(Integer status) {
        shopCache.setStatus(status);
    }

    @Override
    public Integer getStatus() {
        Integer status = shopCache.getStatus();
        return status;
    }
}
