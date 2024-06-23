package com.gyhr.web.system.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Data   //自动生成get & set 方法
@TableName("sys_user")  //指名对应的数据库表示 sys_user表
public class User implements UserDetails {

    @TableField(exist = false)
    private String roleName;

    // 设置主键自增
    @TableId(type = IdType.AUTO)
    private Long userId;
    // 姓名
    private String uname;
    // 电话
    private String uphone;
    // 性别 0女 1男
    private String sex;
    // 身份证
    private String idCard;
    // 是否是管理员 0不是 1是
    private String isAdmin;
    // 在职状态 0在职 1离职
    private String status;
    // 登录名
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
