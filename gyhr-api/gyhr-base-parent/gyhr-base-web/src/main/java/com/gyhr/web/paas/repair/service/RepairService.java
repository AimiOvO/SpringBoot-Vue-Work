package com.gyhr.web.paas.repair.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.paas.repair.entity.Repair;

/**
 * (paas_repair)表服务接口
 *
 * @author makejava
 * @since 2024-02-14 16:31:44
 */
public interface RepairService extends IService<Repair>{
    IPage<Repair> getList(IPage<Repair> page,
                          String repairTitle, String repairContent,
                          String cname, String phone, Long customerId, Long userId);
}
