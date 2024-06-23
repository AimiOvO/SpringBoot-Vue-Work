package com.gyhr.web.fee.parkfee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.parkfee.entity.Parkfee;
import com.gyhr.web.fee.parkfee.service.ParkfeeService;
import com.gyhr.web.fee.parkfee.entity.ParkfeeParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * 停车费表(fee_parkfee)表控制层
 *
 * @author makejava
 * @since 2024-02-13 13:04:30
 */
@RestController
@RequestMapping("/api/feePark")
public class ParkfeeController {
    
    @Autowired
    private ParkfeeService parkfeeService;

    /**
     * 查询待缴费总数
     * @param customerId
     * @return
     */
    @GetMapping("/myParkFeeCount")
    public ResultVo<Object> myParkFeeCount(@RequestParam("customerId") Long customerId) {
        QueryWrapper<Parkfee> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Parkfee::getCustomerId, customerId)
                .eq(Parkfee::getPayStatus, "0");
        int count = parkfeeService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 查询停车费列表
     * @param parm
     * @return
     */
    @GetMapping("/parkfeelist")
    public ResultVo<Object> parkfeelist(ParkfeeParm parm) {
        IPage<Parkfee> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Parkfee> list = parkfeeService.getList(page, parm.getCname(),
                parm.getParkName(), parm.getParkType(), parm.getPayStatus(), parm.getCustomerId());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增电费
     * @param parkfee
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:parkfee:add')")
    public ResultVo<Object> addParkfeeFee(@RequestBody Parkfee parkfee) {
        boolean saved = parkfeeService.saveFeePark(parkfee);
        if (saved) {
            return ResultUtils.success("新增电费成功!");
        }
        return ResultUtils.error("新增电费失败!");
    }

    /**
     * 更新电费
     * @param parkfee
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:parkfee:edit')")
    public ResultVo<Object> editParkfeeFee(@RequestBody Parkfee parkfee) {
        boolean updated = parkfeeService.updateFeePark(parkfee);
        if (updated) {
            return ResultUtils.success("更新电费成功!");
        }
        return ResultUtils.error("更新电费失败!");
    }

    /**
     * 删除
     * @param parkfeeId
     * @return
     */
    @DeleteMapping("/{parkfeeId}")
    @PreAuthorize("hasAuthority('wy:parkfee:delete')")
    public ResultVo<Object> deleteParkfeeFee(@PathVariable("parkfeeId") Long parkfeeId) {
        // 如果已经缴费就不能删除
        QueryWrapper<Parkfee> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Parkfee::getParkfeeId, parkfeeId)
                .eq(Parkfee::getPayStatus, "1");
        Parkfee one = parkfeeService.getOne(queryWrapper);
        if (one != null) {
            return ResultUtils.error("已缴费，不能删除!");
        }
        // 删除操作
        boolean delete = parkfeeService.removeById(parkfeeId);
        if (delete) {
            return ResultUtils.success("删除电费成功!");
        }
        return ResultUtils.error("删除电费失败!");
    }

    /**
     * 缴费
     * @param parkfee
     * @return
     */
    @PostMapping("/payParkfee")
    @PreAuthorize("hasAuthority('wy:parkfee:pay')")
    public ResultVo<Object> payParkFee(@RequestBody Parkfee parkfee) {
        parkfee.setPayStatus("1");
        // 获取当前时间
        parkfee.setPayTime(new Date());
        // 更新数据
        boolean pay = parkfeeService.updateById(parkfee);
        if (pay) {
            return ResultUtils.success("缴费成功!");
        }
        return ResultUtils.error("缴费失败!");
    }
    
}

