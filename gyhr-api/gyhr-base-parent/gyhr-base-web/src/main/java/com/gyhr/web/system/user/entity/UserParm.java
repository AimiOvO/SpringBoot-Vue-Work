package com.gyhr.web.system.user.entity;

import lombok.Data;

@Data
public class UserParm {
    // 页容量
    private Long pageSize;
    // 当前页
    private Long currentPage;
    // 姓名
    private String uname;
    // 电话
    private String uphone;
    // 用户角色
    private Long roleId;
}
