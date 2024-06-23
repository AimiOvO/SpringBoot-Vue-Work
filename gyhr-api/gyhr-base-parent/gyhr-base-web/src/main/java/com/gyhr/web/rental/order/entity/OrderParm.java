package com.gyhr.web.rental.order.entity;

import lombok.Data;

@Data
public class OrderParm {

    // 房屋
    private String homeNum;
    private String unitId;
    private String buildId;
    // 租户
    private String cname;
    // 租单
    private String orderNum;
    private String orderStatus;
    // 时间
    private String startTime;
    private String endTime;
    // 分页参数
    private Long currentPage;
    private Long pageSize;
}
