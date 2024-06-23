package com.gyhr.web.paas.repair.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RepairParm implements Serializable {

    private String repairTitle;
    private String repairContent;
    private String phone;
    private Long  customerId;
    private Long  userId;
    private String cname;

    // 分页参数
    private Long currentPage;
    private Long pageSize;

}
