package com.gyhr.web.rental.customer.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.rental.customer.entity.Customer;

/**
 * 业主表(Customer)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-09 21:55:12
 */
public interface CustomerMapper extends BaseMapper<Customer>{
    IPage<Customer> getList(IPage<Customer> page, @Param("cname") String cname, @Param("cphone") String cphone);
}

