package com.gyhr.web.system.role.entity;

import com.gyhr.web.system.menu.entity.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RolePermissionVo {
    //当前登录系统用户的菜单数据
    List<Menu> listmenu = new ArrayList<>();
    //角色原来分配的菜单
    private Object[] checkList;
}
