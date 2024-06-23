package com.gyhr.web.rental.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.rental.customer.entity.Customer;

/**
 * 业主表(rental_customer)表服务接口
 *
 * @author makejava
 * @since 2024-02-09 21:55:13
 */
public interface CustomerService extends IService<Customer>{

    boolean saveCustomer(Customer customer);
    IPage<Customer> getList(IPage<Customer> page, String cname, String cphone);

    boolean deleteCustomer(Long customerId);

    Customer loadCustomer(String username);
}
