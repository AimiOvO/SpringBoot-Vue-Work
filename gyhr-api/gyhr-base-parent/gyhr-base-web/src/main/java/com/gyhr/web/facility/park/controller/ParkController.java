package com.gyhr.web.facility.park.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.facility.park.entity.Park;
import com.gyhr.web.facility.park.entity.ParkParm;
import com.gyhr.web.facility.park.service.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 车位表(facility_park)表控制层
 *
 * @author makejava
 * @since 2024-02-09 14:53:06
 */
@RestController
@RequestMapping("/api/park")
public class ParkController {

    @Autowired
    private ParkService parkService;

    /**
     * 查询车位列表
     * @param parm
     * @return
     */
    @GetMapping("/parklist")
    public ResultVo<Object> parklist(ParkParm parm) {
        IPage<Park> list = parkService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增车位
     * @param park
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:park:add')")
    public ResultVo<Object> addPark(@RequestBody Park park) {
        boolean save = parkService.save(park);
        if(save){
            return ResultUtils.success("新增车位成功!");
        }
        return ResultUtils.error("新增车位失败!");
    }

    /**
     * 编辑车位
     * @param park
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:park:edit')")
    public ResultVo<Object> editPark(@RequestBody Park park) {
        boolean edit = parkService.updateById(park);
        if(edit){
            return ResultUtils.success("编辑车位成功!");
        }
        return ResultUtils.error("编辑车位失败!");
    }

    @DeleteMapping("/{parkId}")
    @PreAuthorize("hasAuthority('wy:park:delete')")
    public ResultVo<Object> deletePark(@PathVariable("parkId") Long parkId) {
        boolean delete = parkService.removeById(parkId);
        if(delete){
            return ResultUtils.success("删除车位成功!");
        }
        return ResultUtils.error("删除车位失败!");
    }
}

