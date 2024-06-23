package com.gyhr.web.system.roleMenu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.system.roleMenu.entity.RoleMenu;

import java.util.List;

/**
 * 角色菜单表(sys_role_menu)表服务接口
 *
 * @author makejava
 * @since 2024-02-07 16:26:33
 */
public interface RoleMenuService extends IService<RoleMenu>{

//    保存权限
    void saveRoleMenu(Long roleId, List<Long> menuIds);
}
