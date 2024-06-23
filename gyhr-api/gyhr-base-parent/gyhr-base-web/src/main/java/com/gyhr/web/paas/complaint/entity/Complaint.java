package com.gyhr.web.paas.complaint.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * 投诉表(Complaint)实体类
 *
 * @author makejava
 * @since 2024-02-14 15:16:32
 */
@Data   //自动生成get & set 方法
@TableName("paas_complaint")  //指名对应的数据库表示 paas_complaint表
public class Complaint implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String cname;
    @TableField(exist =  false)
    private String cphone;

    // 投诉id
    @TableId(type = IdType.AUTO)
    private Long complaintId;
    // 投诉人id
    private Long customerId;
    // 标题
    private String title;
    // 投诉内容
    private String content;
    // 投诉时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;
    // 处理状态 0未处理 1已处理
    private String status;
}

