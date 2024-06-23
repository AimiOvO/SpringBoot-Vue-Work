package com.gyhr.web.house.home.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class HomeParm implements Serializable {

    // 楼栋id
    private Long buildId;
    // 单元id
    private Long unitId;
    // 房屋名称
    private String homeNum;
    // 使用状态 0未使用 1已使用
    private String status;
    // 分页参数
    private Long currentPage;
    private Long pageSize;
}
