package com.gyhr.web.house.building.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuildingParm implements Serializable {

    // 页容量
    private Long pageSize;
    // 当前页
    private Long currentPage;
    // 楼栋名称
    private String buildName;
    // 楼栋类型
    private String type;
}
