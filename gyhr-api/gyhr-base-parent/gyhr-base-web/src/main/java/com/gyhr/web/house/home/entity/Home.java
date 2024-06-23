package com.gyhr.web.house.home.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 房屋表(Home)实体类
 *
 * @author makejava
 * @since 2024-02-08 19:33:49
 */
@Data   //自动生成get & set 方法
@TableName("house_home")  //指名对应的数据库表示 house_home表
public class Home implements Serializable {

    // 不属于数据库表中的字段 需要排除
    @TableField(exist = false)
    private Long buildId;
    @TableField(exist = false)
    private String buildName;
    @TableField(exist = false)
    private String unitName;

    // 房屋Id
    @TableId(type = IdType.AUTO)
    private Long homeId;
    // 单元Id
    private Long unitId;
    // 房屋编号
    private String homeNum;
    // 房屋面积
    private String homeArea;
    // 使用状态 0未使用 1已使用
    private String status;
}

