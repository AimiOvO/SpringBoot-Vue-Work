package com.gyhr.web.fee.parkfee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.fee.parkfee.entity.Parkfee;
import com.gyhr.web.fee.parkfee.mapper.ParkfeeMapper;
import com.gyhr.web.fee.parkfee.service.ParkfeeService;
import com.gyhr.web.rental.customerPark.entity.CustomerPark;
import com.gyhr.web.rental.customerPark.mapper.CustomerParkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 停车费表(fee_parkfee)表服务实现类
 *
 * @author makejava
 * @since 2024-02-13 13:04:30
 */
@Service
public class ParkfeeServiceImpl extends ServiceImpl<ParkfeeMapper, Parkfee> implements ParkfeeService {

    @Autowired
    private CustomerParkMapper customerParkMapper;

    @Override
    public boolean saveFeePark(Parkfee parkfee) {
        // 根据车位id查询正在使用该车位的用户
        QueryWrapper<CustomerPark> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPark::getParkId, parkfee.getParkId())
                .eq(CustomerPark::getStatus, "0");
        CustomerPark customerPark = customerParkMapper.selectOne(queryWrapper);
        // 把查询出来的用户Id设置到停车费实体里
        parkfee.setCustomerId(customerPark.getCustomerId());
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddSS");
        String feeNo = "PKFNo" + dateFormat.format(new Date()) + customerPark.getCustomerId();
        parkfee.setFeeNo(feeNo);
        int insert = this.baseMapper.insert(parkfee);

        return insert > 0;
    }

    @Override
    public boolean updateFeePark(Parkfee parkfee) {
//        // 根据车位id查询正在使用该车位的用户
//        QueryWrapper<CustomerPark> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(CustomerPark::getParkId, parkfee.getParkId())
//                .eq(CustomerPark::getStatus, "0");
//        CustomerPark customerPark = customerParkMapper.selectOne(queryWrapper);
//        // 把查询出来的用户Id设置到停车费实体里
//        parkfee.setUserId(customerPark.getUserId());
        // 如果设置为未缴费 则重置缴费时间
        if (Objects.equals(parkfee.getPayStatus(), "0")) {
            parkfee.setPayTime(null);
        }
        int update = this.baseMapper.updateById(parkfee);

        return update > 0;
    }

    @Override
    public IPage<Parkfee> getList(IPage<Parkfee> page, String cname,
                                  String parkName, String parkType, String payStatus, Long customerId) {
        return this.baseMapper.getList(page, cname, parkName, parkType, payStatus, customerId);
    }
}

