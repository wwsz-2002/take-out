package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    @ApiOperation("用户端获取店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer status = shopService.getStatus();
        log.info("用户端获取店铺营业状态：{}",status ==1?"营业中":"打样中");
        return Result.success(status);
    }
}
