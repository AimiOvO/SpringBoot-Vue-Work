package com.gyhr.web.fee.parkfee.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParkfeeParm implements Serializable {

    private String payStatus;
    private Long customerId;
    private String cname;
    private String parkName;
    private String parkType;
    // 分页
    private Long currentPage;
    private Long pageSize;

}
