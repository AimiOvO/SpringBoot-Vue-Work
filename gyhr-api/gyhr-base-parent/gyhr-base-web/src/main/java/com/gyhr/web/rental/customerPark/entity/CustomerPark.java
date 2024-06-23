package com.gyhr.web.rental.customerPark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 租户车位表(CustomerPark)实体类
 *
 * @author makejava
 * @since 2024-02-09 21:49:34
 */
@Data   //自动生成get & set 方法
@TableName("rental_customer_park")  //指名对应的数据库表示 rental_customer_park表
public class CustomerPark implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String cname;
    @TableField(exist =  false)
    private String parkName;
    @TableField(exist =  false)
    private String parkStatus;
    @TableField(exist =  false)
    private Long parkNum;
    @TableField(exist =  false)
    private String parkType;

    // 租户车位Id
    @TableId(type = IdType.AUTO)
    private Long customerParkId;
    // 租单单号
    private String rentalparkNum;
    // 租户Id
    private Long customerId;
    // 车位Id
    private Long parkId;
    // 租凭月数
    private Long month;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;
    // 使用状态 0使用 1结束
    private String status;
}

