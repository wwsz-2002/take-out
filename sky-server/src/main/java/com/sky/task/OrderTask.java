package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单的定时任务类
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟处理一次
    public void processTimeOutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        //查询待付款状态下而且下单时间超过15分钟的订单
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        if(list!=null && !list.isEmpty()){
            for (Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }


    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨一点
    public void processDeliveryOrder(){
        log.info("处理一直处于派送的订单：{}",LocalDateTime.now());
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusHours(-1));
        for (Orders orders : list) {
            orders.setStatus(Orders.COMPLETED);
            orderMapper.update(orders);
        }
    }

}
