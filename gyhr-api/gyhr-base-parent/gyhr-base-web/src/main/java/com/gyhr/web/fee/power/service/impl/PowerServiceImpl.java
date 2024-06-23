package com.gyhr.web.fee.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.fee.power.entity.Power;
import com.gyhr.web.fee.power.mapper.PowerMapper;
import com.gyhr.web.fee.power.service.PowerService;
import com.gyhr.web.rental.order.entity.Order;
import com.gyhr.web.rental.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 电费表(fee_power)表服务实现类
 *
 * @author makejava
 * @since 2024-02-12 14:08:14
 */
@Service
public class PowerServiceImpl extends ServiceImpl<PowerMapper, Power> implements PowerService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean saveFeePower(Power power) {
        // 根据房屋id查询正在使用该房间的用户
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getHomeId, power.getHomeId())
                .eq(Order::getOrderStatus, "0");
        Order order = orderMapper.selectOne(queryWrapper);
        // 把查询出来的用户Id设置到电费实体里
        power.setCustomerId(order.getCustomerId());
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddSS");
        String feeNo = "PWFNo" + dateFormat.format(new Date()) + order.getCustomerId();
        power.setFeeNo(feeNo);
        // 保存电费到数据库
        int insert = this.baseMapper.insert(power);

        return insert > 0;
    }

    @Override
    public boolean updateFeePower(Power power) {
//        // 根据房屋id查询正在使用该房间的用户
//        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(Order::getHomeId, power.getHomeId())
//                .eq(Order::getOrderStatus, "0");
//        Order order = orderMapper.selectOne(queryWrapper);
//        // 把查询出来的用户Id设置到电费实体里
//        power.setUserId(order.getUserId());
        // 如果设置为未缴费 则重置缴费时间
        if (Objects.equals(power.getPayStatus(), "0")) {
            power.setPayTime(null);
        }
        // 保存电费到数据库
        int update = this.baseMapper.updateById(power);

        return update > 0;
    }

    @Override
    public IPage<Power> getList(IPage<Power> page, String cname,
                                String homeNum, String payStatus, Long customerId) {
        return this.baseMapper.getList(page, cname, homeNum, payStatus, customerId);
    }


}

