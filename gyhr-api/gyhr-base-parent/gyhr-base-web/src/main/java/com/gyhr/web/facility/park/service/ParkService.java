package com.gyhr.web.facility.park.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.facility.park.entity.Park;
import com.gyhr.web.facility.park.entity.ParkParm;

/**
 * 车位表(facility_park)表服务接口
 *
 * @author makejava
 * @since 2024-02-09 14:53:05
 */
public interface ParkService extends IService<Park>{
    IPage<Park> getList(ParkParm parm);
}
