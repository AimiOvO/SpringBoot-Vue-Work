package com.gyhr.web.system.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.system.menu.entity.MakeMenuTree;
import com.gyhr.web.system.menu.entity.Menu;
import com.gyhr.web.system.menu.service.MenuService;
import com.gyhr.web.system.role.entity.*;
import com.gyhr.web.system.role.mapper.RoleMapper;
import com.gyhr.web.system.role.service.RoleService;
import com.gyhr.web.system.roleMenu.entity.RoleMenu;
import com.gyhr.web.system.roleMenu.service.RoleMenuService;
import com.gyhr.web.system.user.entity.User;
import com.gyhr.web.system.user.service.UserService;
import com.gyhr.web.system.userRole.entity.UserRole;
import com.gyhr.web.system.userRole.mapper.UserRoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public IPage<Role> getList(RoleParm parm) {
        // 构造分页函数
        IPage<Role> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        // 构造查询条件
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getRoleName())) {
            queryWrapper.lambda().like(Role::getRoleName, parm.getRoleName());
        }
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public RolePermissionVo getAssignTree(RoleAssignParm parm) {
        // 查询当前用户的信息
        User user = userService.getById(parm.getUserId());
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.lambda().eq(UserRole::getUserId, parm.getUserId());
        UserRole userRole = userRoleMapper.selectOne(query);

        // 判断当前用户是否是超级管理员
        // 查询当前用户的所有权限信息
        List<Menu> menus = null;
        if (userRole.getRoleId().equals(3L) || user.getIsAdmin().equals("1")) { // 如果是超级管理员 查询全部权限
            QueryWrapper<Menu> queryWrapper =new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(Menu::getOrderNum);
            menus = menuService.list(queryWrapper);
        } else { // 如果不是 根据用户id查询权限
            menus = menuService.getMenuByUserId(parm.getUserId());
        }
        // 组装成树的格式
        List<Menu> menuList = MakeMenuTree.makeTree(menus, 0L);
        // 根据角色id查询权限
        List<Menu> roleMenuList = menuService.getMenuByRoleId(parm.getRoleId());
        // 需要查询出回显的部分
        List<Long> ids = new ArrayList<>();
        Optional.ofNullable(menus).orElse(new ArrayList<>()).stream().filter(Objects::nonNull).forEach(item -> {
            Optional.ofNullable(roleMenuList).orElse(new ArrayList<>()).stream().filter(Objects::nonNull).forEach(dom -> {
                        // 相等的时候 放到list中
                        if (item.getMenuId().equals(dom.getMenuId())) {
                            ids.add(dom.getMenuId());
                        }
            });
        });
        RolePermissionVo vo = new RolePermissionVo();
        vo.setListmenu(menuList);
        vo.setCheckList(ids.toArray());
        return vo;
    }

    @Override
    @Transactional
    public void saveAssign(RolePermissionParm parm) {
        // 删除原来的权限
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenu::getRoleId, parm.getRoleId());
        roleMenuService.remove(queryWrapper);
        // 保存新的权限
        roleMenuService.saveRoleMenu(parm.getRoleId(), parm.getList());
    }
}
