package com.gyhr.web.house.unit.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UnitParm implements Serializable {

    // 楼栋id
    private Long buildId;
    // 单元名称
    private String unitName;
    private Long currentPage;
    private Long pageSize;

}
