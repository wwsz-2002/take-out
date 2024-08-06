package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status) throws Exception {
        if(status !=1&&status!=0){
            throw new Exception();
        }
        log.info("设置店铺营业状态:{}",status ==1?"营业中":"打样中");
        shopService.setStatus(status);
        return Result.success();
    }

    @ApiOperation("管理端获取店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer status = shopService.getStatus();
        log.info("管理端获取店铺营业状态为：{}",status ==1?"营业中":"打样中");
        return Result.success(status);
    }
}
