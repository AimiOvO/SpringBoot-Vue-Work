package com.gyhr.web.house.building.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.house.building.entity.Building;
import com.gyhr.web.house.building.entity.BuildingParm;

/**
 * 楼栋表(house_building)表服务接口
 *
 * @author makejava
 * @since 2024-02-08 14:41:35
 */
public interface BuildingService extends IService<Building>{
    IPage<Building> getList(BuildingParm parm);
}
