package com.gyhr.web.house.building.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.house.building.entity.Building;
import com.gyhr.web.house.building.entity.BuildingParm;
import com.gyhr.web.house.building.mapper.BuildingMapper;
import com.gyhr.web.house.building.service.BuildingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 楼栋表(house_building)表服务实现类
 *
 * @author makejava
 * @since 2024-02-08 14:41:35
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    /**
     * 分页查询
     * @param parm
     * @return
     */
    @Override
    public IPage<Building> getList(BuildingParm parm) {
        //构造查询条件
        QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getBuildName())) {
            queryWrapper.lambda().like(Building::getBuildName, parm.getBuildName());
        }
        if (StringUtils.isNotEmpty(parm.getType())) {
            queryWrapper.lambda().eq(Building::getType, parm.getType());
        }
        IPage<Building> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        if (parm.getPageSize() != null) {
            page.setSize(parm.getPageSize());
        }

        return this.baseMapper.selectPage(page, queryWrapper);
    }
}

