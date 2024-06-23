package com.gyhr.web.rental.customerPark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.facility.park.entity.Park;
import com.gyhr.web.facility.park.mapper.ParkMapper;
import com.gyhr.web.rental.customerPark.entity.CustomerPark;
import com.gyhr.web.rental.customerPark.mapper.CustomerParkMapper;
import com.gyhr.web.rental.customerPark.service.CustomerParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * 业主车位表(rental_customer_park)表服务实现类
 *
 * @author makejava
 * @since 2024-02-09 23:53:47
 */
@Service
public class CustomerParkServiceImpl extends ServiceImpl<CustomerParkMapper, CustomerPark> implements CustomerParkService {

    @Autowired
    private ParkMapper parkMapper;

    @Override
    @Transactional
    public boolean saveCustomerPark(CustomerPark customerPark) {

        // 如果选择在期 把停车位状态设置为使用
        if (Objects.equals(customerPark.getStatus(), "0")) {
            Park park = new Park();
            park.setParkId(customerPark.getParkId());
            park.setParkStatus("1");
            parkMapper.updateById(park);
        }
        // 保存租单信息
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddss");
        String rentalparkNum = "GYRP" + dateFormat.format(customerPark.getCreateTime()) + customerPark.getCustomerId();
        customerPark.setRentalparkNum(rentalparkNum);
        int insert = this.baseMapper.insert(customerPark);

        return insert > 0;
    }

    @Override
    @Transactional
    public boolean editCustomerPark(CustomerPark customerPark) {
        QueryWrapper<CustomerPark> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPark::getCustomerParkId, customerPark.getCustomerParkId());
        Long orgparkId = this.baseMapper.selectOne(queryWrapper).getParkId();
        Park park = new Park();
        // 如果更换原来的停车位 则把原来的停车位改为未使用
        if (!Objects.equals(customerPark.getParkId(), orgparkId)) {
            park.setParkId(orgparkId);
            park.setParkStatus("0");
            parkMapper.updateById(park);
        }
        // 如果选择在期 把停车位状态设置为使用
        if (Objects.equals(customerPark.getStatus(), "0")) {
            park.setParkId(customerPark.getParkId());
            park.setParkStatus("1");
            parkMapper.updateById(park);
        }

        int update = this.baseMapper.updateById(customerPark);

        return update > 0;
    }

    @Override
    @Transactional
    public boolean deleteCustomerPark(Long customerParkId) {
        // 把停车位状态设置为未使用
        Park park = new Park();
        park.setParkId(this.baseMapper.selectById(customerParkId).getParkId());
        park.setParkStatus("0");
        parkMapper.updateById(park);

        int delete = this.baseMapper.deleteById(customerParkId);

        return delete > 0;
    }

    @Override
    @Transactional
    public boolean returnPark(CustomerPark customerPark) {
        // 更新车位状态为未使用
        Park park = new Park();
        park.setParkId(customerPark.getParkId());
        park.setParkStatus("0");
        parkMapper.updateById(park);
        // 更新租单状态为结束
        customerPark.setStatus("1");
        int update = this.baseMapper.updateById(customerPark);

        return update > 0;
    }

    @Override
    public IPage<CustomerPark> getList(IPage<CustomerPark> page, String cname,
                                       String parkName, String parkType,
                                       String rentalparkNum, String status,
                                       String startTime, String endTime) {
        return this.baseMapper.getList(page, cname, parkName, parkType, rentalparkNum, status, startTime, endTime);
    }

    @Override
    public IPage<CustomerPark> getOnParkList(IPage<CustomerPark> page, String parkName, String parkType) {
        return this.baseMapper.getOnParkList(page, parkName, parkType);
    }
}

