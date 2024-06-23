package com.gyhr.web.paas.notice.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
 * 公告表(Notice)实体类
 *
 * @author makejava
 * @since 2024-02-15 15:22:12
 */
@Data   //自动生成get & set 方法
@TableName("paas_notice")  //指名对应的数据库表示 paas_notice表
public class Notice implements Serializable {

    //不属于表的字段，需要排除
    @TableField(exist =  false)
    private String uname;

    // 公告id
    @TableId(type = IdType.AUTO)
    private Long noticeId;
    // 用户id
    private Long userId;
    // 公告标题
    private String title;
    // 公告内容
    private String content;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;
}

