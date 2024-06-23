package com.gyhr.web.system.role.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.system.role.entity.*;
import com.gyhr.web.system.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理控制器
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 获取部门列表
    @GetMapping("/rolelist")
    public ResultVo<Object> rolelist(RoleParm parm) {
        IPage<Role> list = roleService.getList(parm);
        return ResultUtils.success("查询成功", list);
    }

    // 新增部门
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public ResultVo<Object> addRole(@RequestBody Role role) {
        boolean save = roleService.save(role);
        if (save) {
            return ResultUtils.success("新增部门成功！");
        }
        return ResultUtils.error("新增部门失败！");
    }

    // 编辑部门
    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public ResultVo<Object> editRole(@RequestBody Role role) {
        boolean edit = roleService.updateById(role);
        if (edit) {
            return ResultUtils.success("编辑部门成功！");
        }
        return ResultUtils.error("编辑部门失败！");
    }

    // 删除部门
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public ResultVo<Object> deleteRole(@PathVariable("roleId") Long roleId) {
        boolean delete = roleService.removeById(roleId);
        if (delete) {
            return ResultUtils.success("删除部门成功！");
        }
        return ResultUtils.error("删除部门失败！");
    }

    // 查询分配权限树回显
    @GetMapping("/getAssignTree")
    public ResultVo<Object> getAssignTree(RoleAssignParm parm) {
        RolePermissionVo assignTree = roleService.getAssignTree(parm);
        return ResultUtils.success("查询成功", assignTree);
    }

    // 分配权限保存
    @PostMapping("/saveAssignTree")
    @PreAuthorize("hasAuthority('sys:role:saveassign')")
    public ResultVo<Object> saveAssignTree(@RequestBody RolePermissionParm parm) {
        roleService.saveAssign(parm);
        return ResultUtils.success("分配权限成功！");
    }
}
