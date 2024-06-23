package com.gyhr.web.house.home.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.house.home.entity.Home;
import com.gyhr.web.house.home.entity.HomeParm;

/**
 * 房屋表(house_home)表服务接口
 *
 * @author makejava
 * @since 2024-02-08 19:33:51
 */
public interface HomeService extends IService<Home>{
    IPage<Home> getList(HomeParm homeParm);
}
