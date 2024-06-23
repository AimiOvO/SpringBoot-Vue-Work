package com.gyhr.web.system.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.system.menu.entity.Menu;

import java.util.List;

/**
 * 菜单表(sys_menu)表服务接口
 *
 * @author makejava
 * @since 2024-02-05 16:52:23
 */
public interface MenuService extends IService<Menu>{

    // 上级菜单查询
    List<Menu> getParentList();
    // 查询菜单列表
    List<Menu> getList();
    List<Menu> getMenuByUserId(Long userId);
    // 根据角色id查询权限
    List<Menu> getMenuByRoleId(Long roleId);
    List<Menu> getMenuByUserIdForCustomer(Long customerId);

}
