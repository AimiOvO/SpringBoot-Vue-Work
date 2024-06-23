package com.gyhr.web.fee.rental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.fee.rental.entity.Rental;
import com.gyhr.web.fee.rental.mapper.RentalMapper;
import com.gyhr.web.fee.rental.service.RentalService;
import com.gyhr.web.rental.order.entity.Order;
import com.gyhr.web.rental.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 租费表(fee_rental)表服务实现类
 *
 * @author makejava
 * @since 2024-02-12 22:10:04
 */
@Service
public class RentalServiceImpl extends ServiceImpl<RentalMapper, Rental> implements RentalService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean saveFeeRental(Rental rental) {
        // 根据房屋id查询正在使用该房间的用户
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getHomeId, rental.getHomeId())
                .eq(Order::getOrderStatus, "0");
        Order order = orderMapper.selectOne(queryWrapper);
        // 把查询出来的用户Id设置到电费实体里
        rental.setCustomerId(order.getCustomerId());
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddSS");
        String feeNo = "RTFNo" + dateFormat.format(new Date()) + order.getCustomerId();
        rental.setFeeNo(feeNo);
        // 保存电费到数据库
        int insert = this.baseMapper.insert(rental);

        return insert > 0;
    }

    @Override
    public boolean updateFeeRental(Rental rental) {
//        // 根据房屋id查询正在使用该房间的用户
//        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(Order::getHomeId, rental.getHomeId())
//                .eq(Order::getOrderStatus, "0");
//        Order order = orderMapper.selectOne(queryWrapper);
//        // 把查询出来的用户Id设置到电费实体里
//        rental.setUserId(order.getUserId());
        // 如果设置为未缴费 则重置缴费时间
        if (Objects.equals(rental.getPayStatus(), "0")) {
            rental.setPayTime(null);
        }
        // 保存电费到数据库
        int update = this.baseMapper.updateById(rental);

        return update > 0;
    }

    @Override
    public IPage<Rental> getList(IPage<Rental> page, String cname,
                                 String homeNum, String payStatus, Long userId) {
        return this.baseMapper.getList(page, cname, homeNum, payStatus, userId);
    }
}

