package com.gyhr.web.fee.water.entity;

import lombok.Data;

@Data
public class WaterParm {

    private String payStatus;
    private Long customerId;
    private String cname;
    private String homeNum;
    // 分页
    private Long currentPage;
    private Long pageSize;

}
