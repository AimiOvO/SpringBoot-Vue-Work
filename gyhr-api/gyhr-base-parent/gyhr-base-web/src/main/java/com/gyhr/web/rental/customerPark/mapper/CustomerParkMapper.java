package com.gyhr.web.rental.customerPark.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.rental.customerPark.entity.CustomerPark;

/**
 * 业主车位表(CustomerPark)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-09 21:49:35
 */
public interface CustomerParkMapper extends BaseMapper<CustomerPark>{

    IPage<CustomerPark> getList(IPage<CustomerPark> page, @Param("cname") String cname,
        @Param("parkName") String parkName, @Param("parkType") String parkType,
        @Param("rentalparkNum") String rentalparkNum, @Param("status") String status,
        @Param("startTime") String startTime, @Param("endTime") String endTime);
    IPage<CustomerPark> getOnParkList(IPage<CustomerPark> page, @Param("parkName") String parkName,
        @Param("parkType") String parkType);
}

