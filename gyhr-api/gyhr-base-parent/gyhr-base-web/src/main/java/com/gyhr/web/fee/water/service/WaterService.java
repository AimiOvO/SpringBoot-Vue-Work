package com.gyhr.web.fee.water.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.fee.water.entity.Water;

/**
 * 水费表(fee_water)表服务接口
 *
 * @author makejava
 * @since 2024-02-12 20:58:48
 */
public interface WaterService extends IService<Water>{
    boolean saveFeeWater(Water water);
    boolean updateFeeWater(Water water);

    IPage<Water> getList(IPage<Water> page, String cname,
                         String homeNum, String payStatus, Long customerId);

}
