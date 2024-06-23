package com.gyhr.web.rental.customerPark.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.rental.customerPark.entity.CustomerPark;

/**
 * 业主车位表(rental_customer_park)表服务接口
 *
 * @author makejava
 * @since 2024-02-09 23:53:45
 */
public interface CustomerParkService extends IService<CustomerPark>{

    boolean saveCustomerPark(CustomerPark customerPark);
    boolean editCustomerPark(CustomerPark customerPark);
    boolean deleteCustomerPark(Long customerParkId);
    boolean returnPark(CustomerPark customerPark);

    IPage<CustomerPark> getList(IPage<CustomerPark> page, String cname,
                                String parkName, String parkType,
                                String rentalparkNum, String status,
                                String startTime, String endTime);

    IPage<CustomerPark> getOnParkList(IPage<CustomerPark> page, String parkName, String parkType);
}
