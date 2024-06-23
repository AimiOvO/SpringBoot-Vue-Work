package com.gyhr.web.fee.rental.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.rental.entity.Rental;
import com.gyhr.web.fee.rental.entity.RentalParm;
import com.gyhr.web.fee.rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 租费表(fee_rental)表控制层
 *
 * @author makejava
 * @since 2024-02-12 22:10:05
 */
@RestController
@RequestMapping("/api/feeRental")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    /**
     * 查询待缴租凭费总数
     * @param customerId
     * @return
     */
    @GetMapping("/myRentalFeeCount")
    public ResultVo<Object> myRentalFeeCount(@RequestParam("customerId") Long customerId) {
        QueryWrapper<Rental> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Rental::getCustomerId, customerId)
                .eq(Rental::getPayStatus, "0");
        int count = rentalService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 查询列表
     * @param parm
     * @return
     */
    @GetMapping("/rentalfeelist")
    public ResultVo<Object> rentalfeelist(RentalParm parm) {
        IPage<Rental> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Rental> list = rentalService.getList(page, parm.getCname(),
                parm.getHomeNum(), parm.getPayStatus(), parm.getCustomerId());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增租费
     * @param rental
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:rentalfee:add')")
    public ResultVo<Object> addRentalFee(@RequestBody Rental rental) {
        boolean saved = rentalService.saveFeeRental(rental);
        if (saved) {
            return ResultUtils.success("新增租费成功!");
        }
        return ResultUtils.error("新增租费失败!");
    }

    /**
     * 更新租费
     * @param rental
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:rentalfee:edit')")
    public ResultVo<Object> editRentalFee(@RequestBody Rental rental) {
        boolean updated = rentalService.updateFeeRental(rental);
        if (updated) {
            return ResultUtils.success("更新租费成功!");
        }
        return ResultUtils.error("更新租费失败!");
    }

    /**
     * 删除
     * @param rentalfeeId
     * @return
     */
    @DeleteMapping("/{rentalfeeId}")
    @PreAuthorize("hasAuthority('wy:rentalfee:delete')")
    public ResultVo<Object> deleteRentalFee(@PathVariable("rentalfeeId") Long rentalfeeId) {
        // 如果已经缴费就不能删除
        QueryWrapper<Rental> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Rental::getRentalfeeId, rentalfeeId)
                .eq(Rental::getPayStatus, "1");
        Rental one = rentalService.getOne(queryWrapper);
        if (one != null) {
            return ResultUtils.error("已缴费，不能删除!");
        }
        // 删除操作
        boolean delete = rentalService.removeById(rentalfeeId);
        if (delete) {
            return ResultUtils.success("删除租费成功!");
        }
        return ResultUtils.error("删除租费失败!");
    }

    /**
     * 缴费
     * @param rental
     * @return
     */
    @PostMapping("/payRental")
    @PreAuthorize("hasAuthority('wy:rentalfee:pay')")
    public ResultVo<Object> payRentalFee(@RequestBody Rental rental) {
        rental.setPayStatus("1");
        // 获取当前时间
        rental.setPayTime(new Date());
        // 更新数据
        boolean pay = rentalService.updateById(rental);
        if (pay) {
            return ResultUtils.success("缴费成功!");
        }
        return ResultUtils.error("缴费失败!");
    }

}

