package com.gyhr.web.facility.park.entity;

import lombok.Data;

@Data
public class ParkParm {

    // 查询字段
    private String parkName;
    private String parkStatus;
    private String parkType;
    // 分页
    private Long currentPage;
    private Long pageSize;

}
