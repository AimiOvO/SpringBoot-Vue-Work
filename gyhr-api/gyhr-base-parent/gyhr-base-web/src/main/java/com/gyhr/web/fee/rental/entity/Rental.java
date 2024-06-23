package com.gyhr.web.fee.rental.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * 租费表(Rental)实体类
 *
 * @author makejava
 * @since 2024-02-12 22:10:02
 */
@Data   //自动生成get & set 方法
@TableName("fee_rental")  //指名对应的数据库表示 fee_rental表
public class Rental implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String homeNum;
    @TableField(exist =  false)
    private String unitName;
    @TableField(exist =  false)
    private String buildName;
    @TableField(exist =  false)
    private String cname;

    // 租费Id
    @TableId(type = IdType.AUTO)
    private Long rentalfeeId;
    // 费用订单编号
    private String feeNo;
    // 支付单编号
    private String payNo;
    // 房屋Id
    private Long homeId;
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

