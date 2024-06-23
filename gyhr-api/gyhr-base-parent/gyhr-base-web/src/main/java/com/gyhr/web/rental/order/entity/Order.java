package com.gyhr.web.rental.order.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * 租单表(Order)实体类
 *
 * @author makejava
 * @since 2024-02-10 18:21:11
 */
@Data   //自动生成get & set 方法
@TableName("rental_order")  //指名对应的数据库表示 rental_order表
public class Order implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String homeNum;
    @TableField(exist =  false)
    private String unitName;
    @TableField(exist =  false)
    private String buildName;
    @TableField(exist =  false)
    private String cname;
    @TableField(exist =  false)
    private String status;

    // 租单Id
    @TableId(type = IdType.AUTO)
    private Long orderId;
    // 租单单号
    private String orderNum;
    // 租户Id
    private Long customerId;
    // 房屋Id
    private Long homeId;
    // 租凭月数
    private Long month;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;
    // 租用状态 0在期 1结束
    private String orderStatus;
}

