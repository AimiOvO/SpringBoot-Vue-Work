package com.gyhr.web.house.unit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.house.unit.entity.Unit;
import com.gyhr.web.house.unit.entity.UnitParm;

/**
 * 单元表(house_unit)表服务接口
 *
 * @author makejava
 * @since 2024-02-08 17:32:10
 */
public interface UnitService extends IService<Unit>{
    IPage<Unit> getList(UnitParm parm);
}
