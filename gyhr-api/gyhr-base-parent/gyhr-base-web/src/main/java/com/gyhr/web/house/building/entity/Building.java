package com.gyhr.web.house.building.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 楼栋表(Building)实体类
 *
 * @author makejava
 * @since 2024-02-08 14:41:33
 */
@Data   //自动生成get & set 方法
@TableName("house_building")  //指名对应的数据库表示 house_building表
public class Building implements Serializable {
    
    // 楼栋Id
    @TableId(type = IdType.AUTO)
    private Long buildId;
    // 楼栋类型 0普通房 1电梯房
    private String type;
    // 楼栋名称
    private String buildName;
    // 楼栋序号
    private Long orderNum;
}

