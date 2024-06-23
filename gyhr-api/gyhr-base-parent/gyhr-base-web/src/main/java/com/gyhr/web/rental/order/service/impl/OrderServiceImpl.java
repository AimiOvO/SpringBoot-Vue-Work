package com.gyhr.web.rental.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.house.home.entity.Home;
import com.gyhr.web.house.home.mapper.HomeMapper;
import com.gyhr.web.rental.order.entity.Order;
import com.gyhr.web.rental.order.mapper.OrderMapper;
import com.gyhr.web.rental.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * 租单表(rental_order)表服务实现类
 *
 * @author makejava
 * @since 2024-02-10 18:21:13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    @Transactional
    public boolean saveOrder(Order order) {

        // 如果选择在期 把房屋状态设置为使用
        if (Objects.equals(order.getOrderStatus(), "0")) {
            Home home = new Home();
            home.setHomeId(order.getHomeId());
            home.setStatus("1");
            homeMapper.updateById(home);
        }

        // 保存租单信息
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddss");
        String orderName = "GYRO" + dateFormat.format(order.getCreateTime()) + order.getCustomerId();
        order.setOrderNum(orderName);
        int insert = this.baseMapper.insert(order);

        return insert > 0;
    }

    @Override
    @Transactional
    public boolean editOrder(Order order) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getOrderId, order.getOrderId());
        Long orgHomeId = this.baseMapper.selectOne(queryWrapper).getHomeId();
        Home home = new Home();
        // 如果更换原来的房屋 则把原来的房屋改为未使用
        if (!Objects.equals(order.getHomeId(), orgHomeId)){
            home.setHomeId(orgHomeId);
            home.setStatus("0");
            homeMapper.updateById(home);
        }
        // 如果选择在期 把房屋状态设置为使用
        if (Objects.equals(order.getOrderStatus(), "0")) {
            home.setHomeId(order.getHomeId());
            home.setStatus("1");
            homeMapper.updateById(home);
        }
        int update = this.baseMapper.updateById(order);

        return update > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long orderId) {
        // 把房间设为未使用
        Home home = new Home();
        home.setHomeId(this.baseMapper.selectById(orderId).getHomeId());
        home.setStatus("0");
        homeMapper.updateById(home);

        int delete = this.baseMapper.deleteById(orderId);

        return delete > 0;
    }

    @Override
    @Transactional
    public boolean returnHome(Order order) {
        // 更新房屋状态为未使用
        Home home = new Home();
        home.setHomeId(order.getHomeId());
        home.setStatus("0");
        homeMapper.updateById(home);
        // 更新租单状态为结束
        order.setOrderStatus("1");
        int update = this.baseMapper.updateById(order);

        return update > 0;
    }

    @Override
    public IPage<Order> getList(IPage<Order> page,
                                String cname,
                                String homeNum, String unitId, String buildId,
                                String orderNum, String orderStatus, String startTime, String endTime) {
        return this.baseMapper.getList(page,
                cname,
                homeNum, unitId, buildId,
                orderNum, orderStatus,startTime, endTime);
    }

    @Override
    public IPage<Order> getOnHomeList(IPage<Order> page, String homeNum, String unitId, String buildId) {
        return this.baseMapper.getOnHomeList(page, homeNum, unitId, buildId);
    }
}

