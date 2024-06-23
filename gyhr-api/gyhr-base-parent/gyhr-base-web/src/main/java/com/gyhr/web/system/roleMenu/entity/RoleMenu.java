package com.gyhr.web.system.roleMenu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 角色菜单表(RoleMenu)实体类
 *
 * @author makejava
 * @since 2024-02-07 16:26:33
 */
@Data   //自动生成get & set 方法
@TableName("sys_role_menu")  //指名对应的数据库表示 sys_role_menu表
public class RoleMenu implements Serializable {

    // 部门菜单id
    @TableId(type = IdType.AUTO)
    private Long roleMenuId;
    // 部门角色id
    private Long roleId;
    // 用户ID
    private Long menuId;
}

