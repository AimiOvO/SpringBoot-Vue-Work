package com.gyhr.web.fee.rental.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.fee.rental.entity.Rental;

/**
 * 租费表(Rental)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-12 22:10:04
 */
public interface RentalMapper extends BaseMapper<Rental>{
    IPage<Rental> getList(IPage<Rental> page, @Param("cname") String cname,
        @Param("homeNum") String homeNum, @Param("payStatus") String payStatus, @Param("customerId") Long customerId);
}

