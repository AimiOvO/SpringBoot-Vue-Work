package com.gyhr.web.system.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data   //自动生成get & set 方法
@TableName("sys_role")  //指名对应的数据库表示 sys_role表
public class Role implements Serializable {

    // 部门角色id
    @TableId(type = IdType.AUTO)
    private Long roleId;
    // 部门名称
    private String roleName;
    // 备注
    private String remark;

}

