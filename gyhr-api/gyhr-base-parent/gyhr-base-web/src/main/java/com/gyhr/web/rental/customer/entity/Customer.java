package com.gyhr.web.rental.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

/**
 * 租户表(Customer)实体类
 *
 * @author makejava
 * @since 2024-02-09 21:55:11
 */
@Data   //自动生成get & set 方法
@TableName("rental_customer")  //指名对应的数据库表示 rental_customer表
public class Customer implements UserDetails {

    // 租户Id
    @TableId(type = IdType.AUTO)
    private Long customerId;
    // 租户姓名
    private String cname;
    // 租户电话
    private String cphone;
    // 租户身份证
    private String idCard;
    // 性别 0女 1男
    private String sex;
    // 登录账号
    private String username;
    // 登录密码
    private String password;
    // 删除标志 0未删除 1已删除
    private String delFlag;

    //下面的字段，属于spring security的UserDetails的字段
    //帐户是否过期(1 未过期，0已过期)
    private boolean isAccountNonExpired = true;
    //帐户是否被锁定(1 未锁定，0已锁定)
    private boolean isAccountNonLocked = true;
    //密码是否过期(1 未过期，0已过期)
    private boolean isCredentialsNonExpired = true;
    //帐户是否可用(1 可用，0 删除用户)
    private boolean isEnabled = true;
    //由于authorities不是数据库里面的字段，所以要排除他，不然mybatis-plus找不到该字段会报错
    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;
}

