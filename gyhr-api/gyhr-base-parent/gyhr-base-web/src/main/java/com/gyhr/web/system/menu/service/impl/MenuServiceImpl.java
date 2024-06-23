package com.gyhr.web.system.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.system.menu.entity.MakeMenuTree;
import com.gyhr.web.system.menu.entity.Menu;
import com.gyhr.web.system.menu.mapper.MenuMapper;
import com.gyhr.web.system.menu.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 菜单表(sys_menu)表服务实现类
 *
 * @author makejava
 * @since 2024-02-05 16:52:23
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getParentList() {
        // 上级菜单只需要查询目录和菜单
        String[] types = {"0", "1"};
        // 构造查询条件
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Menu::getType, Arrays.asList(types)).orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.baseMapper.selectList(queryWrapper);
        // 构造顶级树形数据
        Menu menu = new Menu();
        menu.setMenuId(0L);
        menu.setParentId(-1L);
        menu.setMenuLabel("顶级菜单");
        menus.add(menu);
        // 返回构造的树形数据
        return MakeMenuTree.makeTree(menus, -1L);
    }

    @Override
    public List<Menu> getList() {
        // 构造查询条件
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.baseMapper.selectList(queryWrapper);
        // 返回构造的树形数据
        return MakeMenuTree.makeTree(menus, 0L);
    }

    @Override
    public List<Menu> getMenuByUserId(Long userId) {
        return this.baseMapper.getMenuByUserId(userId);
    }

    @Override
    public List<Menu> getMenuByRoleId(Long roleId) {
        return this.baseMapper.getMenuByRoleId(roleId);
    }

    @Override
    public List<Menu> getMenuByUserIdForCustomer(Long customerId) {
        return this.baseMapper.getMenuByUserIdForCustomer(customerId);
    }
}

