package com.gyhr.web.paas.repair.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * (Repair)实体类
 *
 * @author makejava
 * @since 2024-02-14 16:31:42
 */
@Data   //自动生成get & set 方法
@TableName("paas_repair")  //指名对应的数据库表示 paas_repair表
public class Repair implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String cname;
    @TableField(exist =  false)
    private String uname;
    @TableField(exist =  false)
    private String uphone;

    // 维修id
    @TableId(type = IdType.AUTO)
    private Long  repairId;
    // 租户id
    private Long  customerId;
    // 维修人员id
    private Long  userId;
    // 维修标题
    private String repairTitle;
    // 维修地址
    private String repairAddress;
    // 维修内容
    private String repairContent;
    // 联系电话
    private String phone;
    // 报修时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;
    // 图片列表
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String imgurl;
    // 维修状态 0未派修 1已派修 2已维修
    private String status;
}

