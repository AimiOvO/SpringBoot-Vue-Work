package com.gyhr.web.rental.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.rental.order.entity.Order;

/**
 * 租单表(rental_order)表服务接口
 *
 * @author makejava
 * @since 2024-02-10 18:21:13
 */
public interface OrderService extends IService<Order>{
    boolean saveOrder(Order order);

    boolean editOrder(Order order);

    boolean deleteOrder(Long orderId);
    boolean returnHome(Order order);

    IPage<Order> getList(IPage<Order> page,
                         String cname,
                         String homeNum, String unitId, String buildId,
                         String orderNum, String orderStatus, String startTime, String endTime);

    IPage<Order> getOnHomeList(IPage<Order> page,String homeNum, String unitId, String buildId);

}
