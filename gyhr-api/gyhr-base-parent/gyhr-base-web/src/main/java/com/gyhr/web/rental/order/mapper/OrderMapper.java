package com.gyhr.web.rental.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.rental.order.entity.Order;

import org.apache.ibatis.annotations.Param;

/**
 * 租单表(Order)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-10 18:21:13
 */
public interface OrderMapper extends BaseMapper<Order>{
    IPage<Order> getList(IPage<Order> page, @Param("cname") String cname,
        @Param("homeNum") String homeNum, @Param("unitId") String unitId, @Param("buildId") String buildId,
        @Param("orderNum") String orderNum, @Param("orderStatus") String orderStatus,
        @Param("startTime") String startTime, @Param("endTime") String endTime);

    IPage<Order> getOnHomeList(IPage<Order> page, @Param("homeNum") String homeNum, @Param("unitId") String unitId,
        @Param("buildId") String buildId);

}

