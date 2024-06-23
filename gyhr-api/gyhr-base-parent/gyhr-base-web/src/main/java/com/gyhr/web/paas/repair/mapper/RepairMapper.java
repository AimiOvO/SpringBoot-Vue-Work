package com.gyhr.web.paas.repair.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.paas.repair.entity.Repair;

/**
 * (Repair)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-14 16:31:43
 */
public interface RepairMapper extends BaseMapper<Repair>{
    IPage<Repair> getList(IPage<Repair> page,
        @Param("repairTitle") String repairTitle, @Param("repairContent") String repairContent,
        @Param("cname") String cname, @Param("phone") String phone, @Param("customerId") Long customerId,
        @Param("userId") Long userId);
}

