package com.gyhr.web.fee.power.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.fee.power.entity.Power;
import org.apache.ibatis.annotations.Param;

/**
 * 电费表(Power)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-12 14:08:13
 */
public interface PowerMapper extends BaseMapper<Power>{
    IPage<Power> getList(IPage<Power> page, @Param("cname") String cname,
        @Param("homeNum") String homeNum, @Param("payStatus") String payStatus, @Param("customerId") Long customerId);
}

