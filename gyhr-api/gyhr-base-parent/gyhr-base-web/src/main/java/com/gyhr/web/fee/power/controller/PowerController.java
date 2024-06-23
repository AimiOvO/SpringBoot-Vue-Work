package com.gyhr.web.fee.power.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.power.entity.Power;
import com.gyhr.web.fee.power.entity.PowerParm;
import com.gyhr.web.fee.power.service.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;


/**
 * 电费表(fee_power)表控制层
 *
 * @author makejava
 * @since 2024-02-12 14:08:14
 */
@RestController
@RequestMapping("/api/feePower")
public class PowerController {

    @Autowired
    private PowerService powerService;

    /**
     * 查询待缴费电费总数
     * @param customerId
     * @return
     */
    @GetMapping("/myPowerFeeCount")
    public ResultVo<Object> myPowerFeeCount(@RequestParam("customerId") Long customerId) {
        QueryWrapper<Power> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Power::getCustomerId, customerId)
                .eq(Power::getPayStatus, "0");
        int count = powerService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 查询列表
     * @param parm
     * @return
     */
    @GetMapping("/powerfeelist")
    public ResultVo<Object> powerfeelist(PowerParm parm) {
        IPage<Power> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Power> list = powerService.getList(page, parm.getCname(),
                parm.getHomeNum(), parm.getPayStatus(), parm.getCustomerId());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增电费
     * @param power
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:powerfee:add')")
    public ResultVo<Object> addPowerFee(@RequestBody Power power) {
        boolean saved = powerService.saveFeePower(power);
        if (saved) {
            return ResultUtils.success("新增电费成功!");
        }
        return ResultUtils.error("新增电费失败!");
    }

    /**
     * 更新电费
     * @param power
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:powerfee:edit')")
    public ResultVo<Object> editPowerFee(@RequestBody Power power) {
        boolean updated = powerService.updateFeePower(power);
        if (updated) {
            return ResultUtils.success("更新电费成功!");
        }
        return ResultUtils.error("更新电费失败!");
    }

    /**
     * 删除
     * @param powerfeeId
     * @return
     */
    @DeleteMapping("/{powerfeeId}")
    @PreAuthorize("hasAuthority('wy:powerfee:delete')")
    public ResultVo<Object> editPowerFee(@PathVariable("powerfeeId") Long powerfeeId) {
        // 如果已经缴费就不能删除
        QueryWrapper<Power> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Power::getPowerfeeId, powerfeeId)
                .eq(Power::getPayStatus, "1");
        Power one = powerService.getOne(queryWrapper);
        if (one != null) {
            return ResultUtils.error("已缴费，不能删除!");
        }
        // 删除操作
        boolean delete = powerService.removeById(powerfeeId);
        if (delete) {
            return ResultUtils.success("删除电费成功!");
        }
        return ResultUtils.error("删除电费失败!");
    }

    /**
     * 缴费
     * @param power
     * @return
     */
    @PostMapping("/payPower")
    @PreAuthorize("hasAuthority('wy:powerfee:pay')")
    public ResultVo<Object> payPowerFee(@RequestBody Power power) {
        power.setPayStatus("1");
        // 获取当前时间
        power.setPayTime(new Date());
        // 更新数据
        boolean pay = powerService.updateById(power);
        if (pay) {
            return ResultUtils.success("缴费成功!");
        }
        return ResultUtils.error("缴费失败!");
    }
}

