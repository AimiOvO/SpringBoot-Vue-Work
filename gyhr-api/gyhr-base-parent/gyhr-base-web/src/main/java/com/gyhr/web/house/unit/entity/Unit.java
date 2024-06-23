package com.gyhr.web.house.unit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 单元表(Unit)实体类
 *
 * @author makejava
 * @since 2024-02-08 17:32:09
 */
@Data   //自动生成get & set 方法
@TableName("house_unit")  //指名对应的数据库表示 house_unit表
public class Unit implements Serializable {

    // 不属于数据库表中的字段 需要排除
    @TableField(exist = false)
    private String buildName;

    // 单元Id
    @TableId(type = IdType.AUTO)
    private Long unitId;
    // 楼栋Id
    private Long buildId;
    // 单元名称
    private String unitName;
}

