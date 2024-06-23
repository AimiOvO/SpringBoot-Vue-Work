package com.gyhr.web.rental.customer.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerParm implements Serializable {

    // 查询列表
    private String cname;
    // 租户电话
    private String cphone;
    // 分页参数
    private Long currentPage;
    private Long pageSize;

}
