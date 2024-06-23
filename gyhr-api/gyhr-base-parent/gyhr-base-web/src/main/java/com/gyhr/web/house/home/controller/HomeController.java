package com.gyhr.web.house.home.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.house.home.entity.Home;
import com.gyhr.web.house.home.entity.HomeParm;
import com.gyhr.web.house.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 房屋表(house_home)表控制层
 *
 * @author makejava
 * @since 2024-02-08 19:33:52
 */
@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取房屋列表
     * @param parm
     * @return
     */
    @GetMapping("/homelist")
    public ResultVo<Object> homelist(HomeParm parm) {
        IPage<Home> list = homeService.getList(parm);
        return ResultUtils.success("查询成功", list);
    }

    /**
     * 新增房屋
     * @param home
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:home:add')")
    public ResultVo<Object> addHome(@RequestBody Home home) {
        boolean save = homeService.save(home);
        if(save){
            return ResultUtils.success("新增房屋成功!");
        }
        return ResultUtils.error("新增房屋失败!");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('wy:home:edit')")
    public ResultVo<Object> editHome(@RequestBody Home home) {
        boolean edit = homeService.updateById(home);
        if(edit){
            return ResultUtils.success("编辑房屋成功!");
        }
        return ResultUtils.error("编辑房屋失败!");
    }

    @DeleteMapping("/{homeId}")
    @PreAuthorize("hasAuthority('wy:home:delete')")
    public ResultVo<Object> deletHome(@PathVariable("homeId") String homeId) {
        boolean delete = homeService.removeById(homeId);
        if(delete){
            return ResultUtils.success("删除房屋成功!");
        }
        return ResultUtils.error("删除房屋失败!");
    }

}

