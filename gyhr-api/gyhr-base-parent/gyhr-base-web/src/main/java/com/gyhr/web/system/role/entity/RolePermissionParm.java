package com.gyhr.web.system.role.entity;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionParm {

    private Long roleId;
    List<Long> list;
}
