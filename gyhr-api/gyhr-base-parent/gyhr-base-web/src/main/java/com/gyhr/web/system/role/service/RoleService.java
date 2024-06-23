package com.gyhr.web.system.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.system.role.entity.*;

public interface RoleService extends IService<Role> {

    // 查询角色列表
    IPage<Role> getList(RoleParm parm);

    RolePermissionVo getAssignTree(RoleAssignParm parm);
    void saveAssign(RolePermissionParm parm);
}
