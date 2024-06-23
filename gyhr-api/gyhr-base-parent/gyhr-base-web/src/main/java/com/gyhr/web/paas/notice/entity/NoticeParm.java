package com.gyhr.web.paas.notice.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoticeParm implements Serializable {

    private String title;
    private String uname;
    // 分页参数
    private Long currentPage;
    private Long pageSize;
}
