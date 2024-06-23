package com.gyhr.web.house.unit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.house.unit.entity.Unit;
import com.gyhr.web.house.unit.entity.UnitParm;
import com.gyhr.web.house.unit.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 单元表(house_unit)表控制层
 *
 * @author makejava
 * @since 2024-02-08 17:32:11
 */
@RestController
@RequestMapping("/api/unit")
public class UnitController {

    @Autowired
    private UnitService unitService;

    /**
     * 查询单元列表
     * @param parm
     * @return
     */
    @GetMapping("/unitlist")
    public ResultVo<Object> unitlist(UnitParm parm) {
        IPage<Unit> list = unitService.getList(parm);
        return ResultUtils.success("查询成功", list);
    }

    /**
     * 根据楼栋id查询单元列表
     * @param unit
     * @return
     */
    @GetMapping("/getUnitListByBuildId")
    public ResultVo<Object> getUnitListByBuildId(Unit unit) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Unit::getBuildId, unit.getBuildId());
        List<Unit> list = unitService.list(queryWrapper);
        return ResultUtils.success("查询成功", list);
    }

    /**
     * 新增单元
     * @param unit
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:unit:add')")
    public ResultVo<Object> addUnit(@RequestBody Unit unit) {
        boolean save = unitService.save(unit);
        if(save){
            return ResultUtils.success("新增单元成功!");
        }
        return ResultUtils.error("新增单元失败!");
    }

    /**
     * 编辑单元
     * @param unit
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:unit:edit')")
    public ResultVo<Object> editUnit(@RequestBody Unit unit) {
        boolean edit = unitService.updateById(unit);
        if(edit){
            return ResultUtils.success("编辑单元成功!");
        }
        return ResultUtils.error("编辑单元失败!");
    }

    /**
     * 删除单元
     * @param unitId
     * @return
     */
    @DeleteMapping("/{unitId}")
    @PreAuthorize("hasAuthority('wy:unit:delete')")
    public ResultVo<Object> deleteUnit(@PathVariable("unitId") Long unitId) {
        boolean delete = unitService.removeById(unitId);
        if(delete){
            return ResultUtils.success("删除单元成功!");
        }
        return ResultUtils.error("删除单元失败!");
    }
}

