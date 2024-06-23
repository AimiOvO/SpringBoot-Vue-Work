package com.gyhr.web.fee.water.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.fee.water.entity.Water;
import org.apache.ibatis.annotations.Param;

/**
 * 水费表(Water)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-12 20:58:47
 */
public interface WaterMapper extends BaseMapper<Water>{
    IPage<Water> getList(IPage<Water> page, @Param("cname") String cname,
        @Param("homeNum") String homeNum, @Param("payStatus") String payStatus, @Param("customerId") Long customerId);
}

