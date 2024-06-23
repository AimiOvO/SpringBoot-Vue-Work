package com.gyhr.web.rental.customerRole.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 租户角色表(CustomerRole)实体类
 *
 * @author makejava
 * @since 2024-02-09 21:56:57
 */
@Data   //自动生成get & set 方法
@TableName("rental_customer_role")  //指名对应的数据库表示 rental_customer_role表
public class CustomerRole implements Serializable {
    
    // 租户角色Id
    @TableId(type = IdType.AUTO)
    private Long customerRoleId;
    // 租户Id
    private Long customerId;
    // 角色Id
    private Long roleId;
}

