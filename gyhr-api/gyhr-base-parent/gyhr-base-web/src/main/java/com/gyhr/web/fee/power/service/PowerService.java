package com.gyhr.web.fee.power.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.fee.power.entity.Power;

/**
 * 电费表(fee_power)表服务接口
 *
 * @author makejava
 * @since 2024-02-12 14:08:13
 */
public interface PowerService extends IService<Power>{

    boolean saveFeePower(Power power);
    boolean updateFeePower(Power power);

    IPage<Power> getList(IPage<Power> page, String cname,
                         String homeNum, String payStatus, Long customerId);

}
