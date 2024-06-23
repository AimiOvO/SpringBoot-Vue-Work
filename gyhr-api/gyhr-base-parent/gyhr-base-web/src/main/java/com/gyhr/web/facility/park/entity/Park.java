package com.gyhr.web.facility.park.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 车位表(Park)实体类
 *
 * @author makejava
 * @since 2024-02-09 14:53:04
 */
@Data   //自动生成get & set 方法
@TableName("facility_park")  //指名对应的数据库表示 facility_park表
public class Park implements Serializable {
    
    // 车位Id
    @TableId(type = IdType.AUTO)
    private Long parkId;
    // 车位类型 0地上 1地下
    private String parkType;
    // 车位名称
    private String parkName;
    // 车位使用状态 0未使用 1已使用
    private String parkStatus;
    // 车位序号
    private Long parkNum;
}

