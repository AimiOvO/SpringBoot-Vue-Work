package com.gyhr.web.house.unit.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.house.unit.entity.Unit;
import com.gyhr.web.house.unit.entity.UnitParm;
import com.gyhr.web.house.unit.mapper.UnitMapper;
import com.gyhr.web.house.unit.service.UnitService;
import org.springframework.stereotype.Service;

/**
 * 单元表(house_unit)表服务实现类
 *
 * @author makejava
 * @since 2024-02-08 17:32:11
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Override
    public IPage<Unit> getList(UnitParm parm) {
        IPage<Unit> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.getList(page, parm.getBuildId(), parm.getUnitName());
    }
}

