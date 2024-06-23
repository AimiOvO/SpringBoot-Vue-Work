package com.gyhr.web.fee.power.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PowerParm implements Serializable {

    private String payStatus;
    private Long customerId;
    private String cname;
    private String homeNum;
    // 分页
    private Long currentPage;
    private Long pageSize;
}
