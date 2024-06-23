package com.gyhr.web.system.userRole.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 用户角色表(UserRole)实体类
 *
 * @author makejava
 * @since 2024-02-07 20:16:57
 */
@Data   //自动生成get & set 方法
@TableName("sys_user_role")  //指名对应的数据库表示 sys_user_role表
public class UserRole implements Serializable {
    
    // 用户角色id
    @TableId(type = IdType.AUTO)
    private Long userRoleId;
    // 角色id
    private Long roleId;
    // 用户ID
    private Long userId;
}

