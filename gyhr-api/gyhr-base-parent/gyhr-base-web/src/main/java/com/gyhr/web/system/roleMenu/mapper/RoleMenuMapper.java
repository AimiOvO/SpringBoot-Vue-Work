package com.gyhr.web.system.roleMenu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gyhr.web.system.roleMenu.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-07 16:26:33
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu>{

    // 保存权限
    void saveRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}

