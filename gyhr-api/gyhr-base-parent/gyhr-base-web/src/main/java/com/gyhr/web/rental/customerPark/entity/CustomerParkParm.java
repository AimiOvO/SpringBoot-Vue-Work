package com.gyhr.web.rental.customerPark.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerParkParm implements Serializable {

    private String cname;
    private String parkName;
    private String parkType;
    // 租单
    private String rentalparkNum;
    private String status;
    // 时间
    private String startTime;
    private String endTime;
    // 分页参数
    private Long currentPage;
    private Long pageSize;
}
