package com.gyhr.web.rental.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.power.entity.Power;
import com.gyhr.web.fee.power.service.PowerService;
import com.gyhr.web.fee.rental.entity.Rental;
import com.gyhr.web.fee.rental.service.RentalService;
import com.gyhr.web.fee.water.entity.Water;
import com.gyhr.web.fee.water.service.WaterService;
import com.gyhr.web.rental.order.entity.Order;
import com.gyhr.web.rental.order.entity.OrderParm;
import com.gyhr.web.rental.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 租单表(rental_order)表控制层
 *
 * @author makejava
 * @since 2024-02-10 18:21:13
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private WaterService waterService;
    @Autowired
    private PowerService powerService;
    @Autowired
    private RentalService rentalService;

    /**
     * 在期租房单总数
     * @return
     */
    @GetMapping("/orderCount")
    public ResultVo<Object> orderCount() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getOrderStatus, "0");
        int count = orderService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 获取租单列表
     * @param parm
     * @return
     */
    @GetMapping("/orderlist")
    public ResultVo<Object> orderlist(OrderParm parm) {
        IPage<Order> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Order> list = orderService.getList(page, parm.getCname(),
                parm.getHomeNum(), parm.getUnitId(), parm.getBuildId(),
                parm.getOrderNum(), parm.getOrderStatus(), parm.getStartTime(), parm.getEndTime());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增租单
     * @param order
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:order:add')")
    public ResultVo<Object> addOrder(@RequestBody Order order) {
        boolean save = orderService.saveOrder(order);
        if (save) {
            return ResultUtils.success("新增租单成功!");
        }

        return ResultUtils.error("新增租单失败!");
    }

    /**
     * 更新租单
     * @param order
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:order:edit')")
    public ResultVo<Object> editOrder(@RequestBody Order order) {
        boolean edit = orderService.editOrder(order);
        if (edit) {
            return ResultUtils.success("编辑租单成功!");
        }
        return ResultUtils.error("编辑租单失败!");
    }

    /**
     * 删除租单
     * @param orderId
     * @return
     */
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('wy:order:delete')")
    public ResultVo<Object> deleteOrder(@PathVariable("orderId") Long orderId) {
        boolean deleted = orderService.deleteOrder(orderId);
        if (deleted) {
            return ResultUtils.success("删除租单成功!");
        }
        return ResultUtils.error("删除租单失败!");
    }

    /**
     * 获取在期状态的房屋列表
     * @param parm
     * @return
     */
    @GetMapping("/getOnHomeList")
    public ResultVo<Object> getOnHomeList(OrderParm parm) {
        IPage<Order> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Order> list = orderService.getOnHomeList(page, parm.getHomeNum(), parm.getUnitId(), parm.getBuildId());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 退房
     * @param order
     * @return
     */
    @PostMapping("/returnHome")
    @PreAuthorize("hasAuthority('wy:order:return')")
    public ResultVo<Object> returnHome(@RequestBody Order order) {
        // 查询水费是否缴清
        QueryWrapper<Water> queryWater = new QueryWrapper<>();
        queryWater.lambda().eq(Water::getHomeId, order.getHomeId())
                .eq(Water::getCustomerId, order.getCustomerId())
                .eq(Water::getPayStatus, "0");
        List<Water> listWater = waterService.list(queryWater);
        if (!listWater.isEmpty()) {
            return ResultUtils.error("请缴清水费之后再退房");
        }
        // 查询电费是否缴清
        QueryWrapper<Power> queryPower = new QueryWrapper<>();
        queryPower.lambda().eq(Power::getHomeId, order.getHomeId())
                .eq(Power::getCustomerId, order.getCustomerId())
                .eq(Power::getPayStatus, "0");
        List<Power> listPower = powerService.list(queryPower);
        if (!listPower.isEmpty()) {
            return ResultUtils.error("请缴清电费之后再退房");
        }
        // 查询租凭费用是否缴清
        QueryWrapper<Rental> queryRental = new QueryWrapper<>();
        queryRental.lambda().eq(Rental::getHomeId, order.getHomeId())
                .eq(Rental::getCustomerId, order.getCustomerId())
                .eq(Rental::getPayStatus, "0");
        List<Rental> listRental = rentalService.list(queryRental);
        if (!listRental.isEmpty()) {
            return ResultUtils.error("请缴清租房费之后再退房");
        }
        // 退房
        boolean returned = orderService.returnHome(order);
        if (returned) {
            return ResultUtils.success("退房成功!");
        }
        return ResultUtils.error("退房失败!");
    }

}

