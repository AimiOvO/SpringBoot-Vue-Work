package com.gyhr.web.paas.repair.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.paas.repair.entity.Repair;
import com.gyhr.web.paas.repair.mapper.RepairMapper;
import com.gyhr.web.paas.repair.service.RepairService;
import org.springframework.stereotype.Service;

/**
 * (paas_repair)表服务实现类
 *
 * @author makejava
 * @since 2024-02-14 16:31:44
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Override
    public IPage<Repair> getList(IPage<Repair> page,
                                 String repairTitle,String repairContent,
                                 String cname, String phone, Long customerId, Long userId) {
        return this.baseMapper.getList(page, repairTitle,repairContent,
                cname, phone, customerId, userId);
    }
}

