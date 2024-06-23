package com.gyhr.web.rental.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.rental.customer.entity.Customer;
import com.gyhr.web.rental.customer.entity.CustomerParm;
import com.gyhr.web.rental.customer.service.CustomerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;


/**
 * 业主表(rental_customer)表控制层
 *
 * @author makejava
 * @since 2024-02-09 21:55:13
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 查询租户总数
     * @return
     */
    @GetMapping("/customerCount")
    public ResultVo<Object> customerCount() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Customer::getDelFlag, "0");
        int count = customerService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 查询租户列表
     * @param parm
     * @return
     */
    @GetMapping("/customerlist")
    public ResultVo<Object> customerlist(CustomerParm parm) {
        IPage<Customer> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        IPage<Customer> list = customerService.getList(page, parm.getCname(), parm.getCphone());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增租户
     * @param customer
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:customer:add')")
    public ResultVo<Object> addCustomer(@RequestBody Customer customer){
        if (StringUtils.isNotEmpty(customer.getUsername())) {
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Customer::getUsername, customer.getUsername());
            if (customerService.getOne(queryWrapper) != null) {
                return ResultUtils.error("登录名已经被占用", 500);
            }
        }
        // 如果密码存在 MD5加密
        if (StringUtils.isNotEmpty(customer.getPassword())) {
//            customer.setPassword(DigestUtils.md5DigestAsHex(customer.getPassword().getBytes()));
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        customer.setDelFlag("0");
        boolean save = customerService.saveCustomer(customer);
        if (save) {
           return ResultUtils.success("新增业主成功!");
        }

        return ResultUtils.error("新增业主失败!");
    }


    /**
     * 编辑业主
     * @param customer
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:customer:edit')")
    public ResultVo<Object> editCustomer(@RequestBody Customer customer){
        if (StringUtils.isNotEmpty(customer.getUsername())) {
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Customer::getUsername, customer.getUsername());
            Customer one = customerService.getOne(queryWrapper);
            if (one != null && !Objects.equals(one.getCustomerId(), customer.getCustomerId())) {
                return ResultUtils.error("登录名已经被占用", 500);
            }
        }
        // 如果密码存在 MD5加密
        if (StringUtils.isNotEmpty(customer.getPassword())) {
//            customer.setPassword(DigestUtils.md5DigestAsHex(customer.getPassword().getBytes()));
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        boolean edit = customerService.updateById(customer);
        if (edit) {
            return ResultUtils.success("编辑业主成功!");
        }

        return ResultUtils.error("编辑业主失败!");
    }

    /**
     * 删除业主
     * @param customerId
     * @return
     */
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAuthority('wy:customer:delete')")
    public ResultVo<Object> deleteCustomer(@PathVariable("customerId") Long customerId){
        boolean delete = customerService.deleteCustomer(customerId);
        if (delete) {
            return ResultUtils.success("删除业主成功!");
        }

        return ResultUtils.error("删除业主失败!请检查该租户是否有进行业务！");
    }

}

