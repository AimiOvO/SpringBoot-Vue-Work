package com.gyhr.web.fee.parkfee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.fee.parkfee.entity.Parkfee;
import org.apache.ibatis.annotations.Param;

/**
 * 停车费表(Parkfee)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-13 13:04:29
 */
public interface ParkfeeMapper extends BaseMapper<Parkfee>{
    IPage<Parkfee> getList(IPage<Parkfee> page, @Param("cname") String cname,
        @Param("parkName") String parkName, @Param("parkType") String parkType,
        @Param("payStatus") String payStatus, @Param("customerId") Long customerId);
}

