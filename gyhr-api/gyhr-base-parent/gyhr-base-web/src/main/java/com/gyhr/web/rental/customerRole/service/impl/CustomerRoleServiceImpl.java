package com.gyhr.web.rental.customerRole.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.rental.customerRole.entity.CustomerRole;
import com.gyhr.web.rental.customerRole.mapper.CustomerRoleMapper;
import com.gyhr.web.rental.customerRole.service.CustomerRoleService;
import org.springframework.stereotype.Service;

/**
 * 业主角色表(rental_customer_role)表服务实现类
 *
 * @author makejava
 * @since 2024-02-09 23:53:53
 */
@Service
public class CustomerRoleServiceImpl extends ServiceImpl<CustomerRoleMapper, CustomerRole> implements CustomerRoleService {

}

