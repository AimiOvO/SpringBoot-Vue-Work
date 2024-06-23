package com.gyhr.web.facility.park.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.facility.park.entity.Park;
import com.gyhr.web.facility.park.entity.ParkParm;
import com.gyhr.web.facility.park.mapper.ParkMapper;
import com.gyhr.web.facility.park.service.ParkService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 车位表(facility_park)表服务实现类
 *
 * @author makejava
 * @since 2024-02-09 14:53:06
 */
@Service
public class ParkServiceImpl extends ServiceImpl<ParkMapper, Park> implements ParkService {

    @Override
    public IPage<Park> getList(ParkParm parm) {
        // 构造分页查询条件
        QueryWrapper<Park> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getParkName())) {
            queryWrapper.lambda().like(Park::getParkName, parm.getParkName());
        }
        if (StringUtils.isNotEmpty(parm.getParkStatus())) {
            queryWrapper.lambda().like(Park::getParkStatus, parm.getParkStatus());
        }
        if (StringUtils.isNotEmpty(parm.getParkType())) {
            queryWrapper.lambda().like(Park::getParkType, parm.getParkType());
        }
        queryWrapper.lambda().orderByAsc(Park::getParkNum);
        // 构造分页对象
        IPage<Park> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());

        return this.baseMapper.selectPage(page, queryWrapper);
    }
}

