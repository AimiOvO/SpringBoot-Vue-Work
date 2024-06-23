package com.gyhr.web.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gyhr.web.system.menu.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-05 16:52:22
 */
public interface MenuMapper extends BaseMapper<Menu>{
    // 根据用户id查询权限
    List<Menu> getMenuByUserId(@Param("userId") Long userId);
    // 根据角色id查询权限
    List<Menu> getMenuByRoleId(@Param("roleId") Long roleId);
    //根据业主的id查询菜单
    List<Menu> getMenuByUserIdForCustomer(@Param("customerId") Long customerId);
}

