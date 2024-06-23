package com.gyhr.web.house.home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.house.home.entity.Home;
import org.apache.ibatis.annotations.Param;

/**
 * 房屋表(Home)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-08 19:33:51
 */
public interface HomeMapper extends BaseMapper<Home>{
    IPage<Home> getList(IPage<Home> page, @Param("buildId") Long buildId, @Param("unitId") Long unitId,
        @Param("homeNum") String homeNum, @Param("status") String status);
}

