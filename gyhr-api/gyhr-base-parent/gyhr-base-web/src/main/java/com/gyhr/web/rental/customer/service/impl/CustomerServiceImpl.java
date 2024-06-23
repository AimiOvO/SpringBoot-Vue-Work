package com.gyhr.web.rental.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.rental.customer.entity.Customer;
import com.gyhr.web.rental.customer.mapper.CustomerMapper;
import com.gyhr.web.rental.customer.service.CustomerService;
import com.gyhr.web.rental.customerRole.entity.CustomerRole;
import com.gyhr.web.rental.customerRole.mapper.CustomerRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业主表(rental_customer)表服务实现类
 *
 * @author makejava
 * @since 2024-02-09 21:55:13
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerRoleMapper customerRoleMapper;

    @Override
    @Transactional
    public boolean saveCustomer(Customer customer) {
        // 保存租户信息
        this.baseMapper.insert(customer);
        // 保存角色信息
        CustomerRole customerRole = new CustomerRole();
        customerRole.setRoleId(2L);
        customerRole.setCustomerId(customer.getCustomerId());
        int insert = customerRoleMapper.insert(customerRole);

        return insert > 0;
    }

    @Override
    public IPage<Customer> getList(IPage<Customer> page, String cname, String cphone) {
        return this.baseMapper.getList(page, cname, cphone);
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Long customerId) {
        // 假删设置用户信息
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setEnabled(false);
        customer.setDelFlag("1");
        // 删除租户信息
        int update = this.baseMapper.updateById(customer);
        // int delete = this.baseMapper.deleteById(userId);

        return update > 0;
    }

    @Override
    public Customer loadCustomer(String username) {
        QueryWrapper<Customer> queryWrapper =new QueryWrapper<>();
        queryWrapper.lambda().eq(Customer::getUsername, username);

        return this.baseMapper.selectOne(queryWrapper);
    }

}

