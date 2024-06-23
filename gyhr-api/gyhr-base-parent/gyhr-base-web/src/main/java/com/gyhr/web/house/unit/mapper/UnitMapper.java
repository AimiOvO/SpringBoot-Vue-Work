package com.gyhr.web.house.unit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.house.unit.entity.Unit;
import org.apache.ibatis.annotations.Param;

/**
 * 单元表(Unit)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-08 17:32:10
 */
public interface UnitMapper extends BaseMapper<Unit>{
    IPage<Unit> getList(IPage<Unit> page, @Param("buildId") Long buildId, @Param("unitName") String unitName);
}

