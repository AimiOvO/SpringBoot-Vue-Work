package com.gyhr.web.rental.customerPark.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.parkfee.entity.Parkfee;
import com.gyhr.web.fee.parkfee.service.ParkfeeService;
import com.gyhr.web.rental.customerPark.entity.CustomerPark;
import com.gyhr.web.rental.customerPark.entity.CustomerParkParm;
import com.gyhr.web.rental.customerPark.service.CustomerParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 租户车位表(rental_customer_park)表控制层
 *
 * @author makejava
 * @since 2024-02-13 13:30:21
 */
@RestController
@RequestMapping("/api/customerPark")
public class CustomerParkController {

    @Autowired
    private CustomerParkService customerParkService;
    @Autowired
    private ParkfeeService parkfeeService;

    /**
     * 获取租单列表
     * @param parm
     * @return
     */
    @GetMapping("/RentalParkList")
    public ResultVo<Object> RentalParkList(CustomerParkParm parm) {
        IPage<CustomerPark> page = new Page<>();
        page.setCurrent(page.getCurrent());
        page.setSize(parm.getPageSize());
        IPage<CustomerPark> list = customerParkService.getList(page, parm.getCname(),
                parm.getParkName(), parm.getParkType(),
                parm.getRentalparkNum(), parm.getStatus(),
                parm.getStartTime(), parm.getEndTime());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增租单
     * @param customerPark
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:rentalpark:add')")
    public ResultVo<Object> addRentalPark(@RequestBody CustomerPark customerPark) {
        boolean save = customerParkService.saveCustomerPark(customerPark);
        if (save) {
            return ResultUtils.success("新增租单成功!");
        }

        return ResultUtils.error("新增租单失败!");
    }

    /**
     * 编辑租单
     * @param customerPark
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:rentalpark:edit')")
    public ResultVo<Object> editRentalPark(@RequestBody CustomerPark customerPark) {
        boolean edit = customerParkService.editCustomerPark(customerPark);
        if (edit) {
            return ResultUtils.success("编辑租单成功!");
        }
        return ResultUtils.error("编辑租单失败!");
    }

    /**
     * 删除租单
     * @param customerParkId
     * @return
     */
    @DeleteMapping("/{customerParkId}")
    @PreAuthorize("hasAuthority('wy:rentalpark:delete')")
    public ResultVo<Object> deletRentalPark(@PathVariable("customerParkId") Long customerParkId) {
        boolean deleted = customerParkService.deleteCustomerPark(customerParkId);
        if (deleted) {
            return ResultUtils.success("删除租单成功!");
        }
        return ResultUtils.error("删除租单失败!");
    }


    /**
     * 获取在期状态的停车位列表
     * @param parm
     * @return
     */
    @GetMapping("/getOnParkList")
    public ResultVo<Object> getOnParkList(CustomerParkParm parm) {
        IPage<CustomerPark> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<CustomerPark> list = customerParkService.getOnParkList(page, parm.getParkName(), parm.getParkType());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 退车位
     * @param customerPark
     * @return
     */
    @PostMapping("/returnPark")
    @PreAuthorize("hasAuthority('wy:rentalpark:return')")
    public ResultVo<Object> returnPark(@RequestBody CustomerPark customerPark) {
        // 查询停车费是否已交清
        QueryWrapper<Parkfee> queryPark = new QueryWrapper<>();
        queryPark.lambda().eq(Parkfee::getParkId, customerPark.getParkId())
                .eq(Parkfee::getCustomerId, customerPark.getCustomerId())
                .eq(Parkfee::getPayStatus, "0");
        List<Parkfee> listPark = parkfeeService.list(queryPark);
        if (!listPark.isEmpty()) {
            return ResultUtils.error("请缴清停车费之后再退车位");
        }
        boolean returned = customerParkService.returnPark(customerPark);
        if (returned) {
            return ResultUtils.success("退车位成功!");
        }
        return ResultUtils.error("退车位失败!");
    }

}

