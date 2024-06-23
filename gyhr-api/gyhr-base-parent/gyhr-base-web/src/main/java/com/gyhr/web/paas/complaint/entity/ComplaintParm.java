package com.gyhr.web.paas.complaint.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ComplaintParm implements Serializable {

    private String title;
    private String content;
    private Long customerId;
    private String cname;
    private String cphone;

    // 分页参数
    private Long currentPage;
    private Long pageSize;


}
