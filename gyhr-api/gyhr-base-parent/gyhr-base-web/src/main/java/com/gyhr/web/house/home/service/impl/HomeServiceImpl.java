package com.gyhr.web.house.home.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.house.home.entity.Home;
import com.gyhr.web.house.home.entity.HomeParm;
import com.gyhr.web.house.home.mapper.HomeMapper;
import com.gyhr.web.house.home.service.HomeService;
import org.springframework.stereotype.Service;

/**
 * 房屋表(house_home)表服务实现类
 *
 * @author makejava
 * @since 2024-02-08 19:33:51
 */
@Service
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Home> implements HomeService {

    @Override
    public IPage<Home> getList(HomeParm parm) {
        IPage<Home> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());

        return this.baseMapper.getList(page, parm.getBuildId(), parm.getUnitId(), parm.getHomeNum(), parm.getStatus());
    }
}

