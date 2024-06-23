package com.gyhr.web.system.menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单表(Menu)实体类
 *
 * @author makejava
 * @since 2024-02-05 16:52:21
 */
@Data   //自动生成get & set 方法
@TableName("sys_menu")  //指名对应的数据库表示 sys_menu表
public class Menu implements Serializable {

    // 不属于数据库表中的字段 需要排除
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

    // 菜单id
    @TableId(type = IdType.AUTO)
    private Long menuId;
    // 父级菜单id
    private Long parentId;
    // 菜单名称
    private String menuLabel;
    // 权限字段
    private String menuCode;
    // 路由名称
    private String name;
    // 路由地址
    private String path;
    // 组件路由
    private String url;
    // 类型 0目录 1菜单 2按钮
    private String type;
    // 图标
    private String icon;
    // 序号
    private Integer orderNum;
    // 备注
    private String remark;
    // 上级菜单名称
    private String parentName;
}

