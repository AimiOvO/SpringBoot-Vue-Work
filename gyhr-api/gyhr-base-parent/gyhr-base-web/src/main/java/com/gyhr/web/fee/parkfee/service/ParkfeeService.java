package com.gyhr.web.fee.parkfee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.fee.parkfee.entity.Parkfee;

/**
 * 停车费表(fee_parkfee)表服务接口
 *
 * @author makejava
 * @since 2024-02-13 13:04:30
 */
public interface ParkfeeService extends IService<Parkfee>{
    boolean saveFeePark(Parkfee parkfee);
    boolean updateFeePark(Parkfee parkfee);

    IPage<Parkfee> getList(IPage<Parkfee> page, String cname,
                           String parkName, String parkType, String payStatus, Long customerId);
}
