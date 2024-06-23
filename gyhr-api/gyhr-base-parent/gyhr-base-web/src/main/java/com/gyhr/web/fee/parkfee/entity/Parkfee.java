package com.gyhr.web.fee.parkfee.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * 停车费表(Parkfee)实体类
 *
 * @author makejava
 * @since 2024-02-13 13:04:28
 */
@Data   //自动生成get & set 方法
@TableName("fee_parkfee")  //指名对应的数据库表示 fee_parkfee表
public class Parkfee implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String cname;
    @TableField(exist =  false)
    private String parkName;
    @TableField(exist =  false)
    private String parkType;

    // 停车费Id
    @TableId(type = IdType.AUTO)
    private Long parkfeeId;
    // 费用订单编号
    private String feeNo;
    // 支付单编号
    private String payNo;
    // 停车位Id
    private Long parkId;
    // 用户Id
    private Long customerId;
    // 缴费年月
    private String payMonth;
    // 缴费金额
    private BigDecimal payMoney;
    // 缴费状态 0未缴费 1已缴费
    private String payStatus;
    // 缴费时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date payTime;
}

