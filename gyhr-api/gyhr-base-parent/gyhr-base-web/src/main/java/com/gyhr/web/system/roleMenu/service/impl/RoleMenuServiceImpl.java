package com.gyhr.web.system.roleMenu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.system.roleMenu.entity.RoleMenu;
import com.gyhr.web.system.roleMenu.mapper.RoleMenuMapper;
import com.gyhr.web.system.roleMenu.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单表(sys_role_menu)表服务实现类
 *
 * @author makejava
 * @since 2024-02-07 16:26:33
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public void saveRoleMenu(Long roleId, List<Long> menuIds) {
        this.baseMapper.saveRoleMenu(roleId, menuIds);
    }
}

