package com.gyhr.web.fee.water.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.fee.water.entity.Water;
import com.gyhr.web.fee.water.mapper.WaterMapper;
import com.gyhr.web.fee.water.service.WaterService;
import com.gyhr.web.rental.order.entity.Order;
import com.gyhr.web.rental.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 水费表(fee_water)表服务实现类
 *
 * @author makejava
 * @since 2024-02-12 20:58:48
 */
@Service
public class WaterServiceImpl extends ServiceImpl<WaterMapper, Water> implements WaterService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean saveFeeWater(Water water) {
        // 根据房屋id查询正在使用该房间的用户
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getHomeId, water.getHomeId())
                .eq(Order::getOrderStatus, "0");
        Order order = orderMapper.selectOne(queryWrapper);
        // 把查询出来的用户Id设置到电费实体里
        water.setCustomerId(order.getCustomerId());
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddSS");
        String feeNo = "WTFNo" + dateFormat.format(new Date()) + order.getCustomerId();
        water.setFeeNo(feeNo);
        // 保存电费到数据库
        int insert = this.baseMapper.insert(water);

        return insert > 0;
    }

    @Override
    public boolean updateFeeWater(Water water) {
//        // 根据房屋id查询正在使用该房间的用户
//        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(Order::getHomeId, water.getHomeId())
//                .eq(Order::getOrderStatus, "0");
//        Order order = orderMapper.selectOne(queryWrapper);
//        // 把查询出来的用户Id设置到电费实体里
//        water.setUserId(order.getUserId());
        // 如果设置为未缴费 则重置缴费时间
        if (Objects.equals(water.getPayStatus(), "0")) {
            water.setPayTime(null);
        }
        // 保存电费到数据库
        int update = this.baseMapper.updateById(water);

        return update > 0;
    }

    @Override
    public IPage<Water> getList(IPage<Water> page, String cname,
                                String homeNum, String payStatus, Long customerId) {
        return this.baseMapper.getList(page, cname, homeNum, payStatus, customerId);
    }
}

