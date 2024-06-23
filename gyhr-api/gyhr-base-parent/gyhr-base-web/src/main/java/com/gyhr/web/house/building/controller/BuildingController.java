package com.gyhr.web.house.building.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.house.building.entity.Building;
import com.gyhr.web.house.building.entity.BuildingParm;
import com.gyhr.web.house.building.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 楼栋表(house_building)表控制层
 *
 * @author makejava
 * @since 2024-02-08 14:41:35
 */
@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    /**
     * 查询楼栋列表
     * @param parm
     * @return
     */
    @GetMapping("/buildlist")
    public ResultVo<Object> buildlist(BuildingParm parm) {
        IPage<Building> list = buildingService.getList(parm);
        return ResultUtils.success("查询成功", list);
    }

    /**
     * 新增楼栋
     * @param building
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:building:add')")
    public ResultVo<Object> addBuilding(@RequestBody Building building) {
        boolean save = buildingService.save(building);
        if (save) {
            return ResultUtils.success("新增楼栋成功!");
        }
        return ResultUtils.error("新增楼栋失败!");
    }

    /**
     * 编辑楼栋
     * @param building
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:building:edit')")
    public ResultVo<Object> editBuilding(@RequestBody Building building) {
        boolean edit = buildingService.updateById(building);
        if (edit) {
            return ResultUtils.success("编辑楼栋成功!");
        }
        return ResultUtils.error("编辑楼栋失败!");
    }

    /**
     * 删除楼栋
     * @param buildId
     * @return
     */
    @DeleteMapping("/{buildId}")
    @PreAuthorize("hasAuthority('wy:building:delete')")
    public ResultVo<Object> deleteBuilding(@PathVariable("buildId") Long buildId) {
        boolean delete = buildingService.removeById(buildId);
        if (delete) {
            return ResultUtils.success("删除楼栋成功!");
        }
        return ResultUtils.error("删除楼栋失败!");
    }
}

