package com.gyhr.web.fee.water.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.water.entity.Water;
import com.gyhr.web.fee.water.entity.WaterParm;
import com.gyhr.web.fee.water.service.WaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * 水费表(fee_water)表控制层
 *
 * @author makejava
 * @since 2024-02-12 20:58:49
 */
@RestController
@RequestMapping("/api/feeWater")
public class WaterController {
    
    @Autowired
    private WaterService waterService;

    /**
     * 获取待缴水费总数
     * @param customerId
     * @return
     */
    @GetMapping("/myWaterFeeCount")
    public ResultVo<Object> myWaterFeeCount(@RequestParam("customerId") Long customerId) {
        QueryWrapper<Water> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Water::getCustomerId, customerId)
                .eq(Water::getPayStatus, "0");
        int count = waterService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 获取列表
     * @param parm
     * @return
     */
    @GetMapping("/waterfeelist")
    public ResultVo<Object> waterfeelist(WaterParm parm) {
        IPage<Water> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Water> list = waterService.getList(page, parm.getCname(),
                parm.getHomeNum(), parm.getPayStatus(), parm.getCustomerId());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增
     * @param water
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:waterfee:add')")
    public ResultVo<Object> addWaterFee(@RequestBody Water water) {
        boolean saved = waterService.saveFeeWater(water);
        if (saved) {
            return ResultUtils.success("新增水费成功!");
        }
        return ResultUtils.error("新增水费失败!");
    }

    /**
     * 更新
     * @param water
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:waterfee:edit')")
    public ResultVo<Object> editWaterFee(@RequestBody Water water) {
        boolean updated = waterService.updateFeeWater(water);
        if (updated) {
            return ResultUtils.success("更新水费成功!");
        }
        return ResultUtils.error("更新水费失败!");
    }

    /**
     * 删除
     * @param waterfeeId
     * @return
     */
    @DeleteMapping("/{waterfeeId}")
    @PreAuthorize("hasAuthority('wy:waterfee:delete')")
    public ResultVo<Object> deleteWaterFee(@PathVariable("waterfeeId") Long waterfeeId) {
        // 如果已经缴费就不能删除
        QueryWrapper<Water> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Water::getWaterfeeId, waterfeeId)
                .eq(Water::getPayStatus, "1");
        Water one = waterService.getOne(queryWrapper);
        if (one != null) {
            return ResultUtils.error("已缴费，不能删除!");
        }
        // 删除操作
        boolean delete = waterService.removeById(waterfeeId);
        if (delete) {
            return ResultUtils.success("删除水费成功!");
        }
        return ResultUtils.error("删除水费失败!");
    }

    /**
     * 缴费
     * @param water
     * @return
     */
    @PostMapping("/payWater")
    @PreAuthorize("hasAuthority('wy:waterfee:pay')")
    public ResultVo<Object> payWaterFee(@RequestBody Water water) {
        water.setPayStatus("1");
        // 获取当前时间
        water.setPayTime(new Date());
        // 更新数据
        boolean pay = waterService.updateById(water);
        if (pay) {
            return ResultUtils.success("缴费成功!");
        }
        return ResultUtils.error("缴费失败!");
    }
    
}

