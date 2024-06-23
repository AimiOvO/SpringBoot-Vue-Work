package com.gyhr.web.system.menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.system.menu.entity.Menu;
import com.gyhr.web.system.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 菜单表(sys_menu)表控制层
 *
 * @author makejava
 * @since 2024-02-05 16:52:23
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 新增菜单
    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public ResultVo<Object> addMenu(@RequestBody Menu menu) {
        boolean save = menuService.save(menu);
        if (save) {
            return ResultUtils.success("新增菜单成功！");
        }
        return ResultUtils.error("新增菜单失败！");
    }

    // 编辑菜单
    @PutMapping
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    public ResultVo<Object> editMenu(@RequestBody Menu menu) {
        boolean edit = menuService.updateById(menu);
        if (edit) {
            return ResultUtils.success("新增菜单成功！");
        }
        return ResultUtils.error("新增菜单失败！");
    }

    // 删除菜单
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public ResultVo<Object> deleteMenu(@PathVariable("menuId") Long menuId) {
        // 如果有下级 不能删除
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Menu::getParentId, menuId);
        List<Menu> list = menuService.list(queryWrapper);
        if (!list.isEmpty()){
            return ResultUtils.error("该菜单存在下级，不能删除！");
        }
        boolean delete = menuService.removeById(menuId);
        if (delete) {
            return ResultUtils.success("删除菜单成功！");
        }
        return ResultUtils.error("删除菜单失败！");
    }

    // 上级部门的查询
    @GetMapping("/parent")
    public ResultVo<Object> getParentList() {
        List<Menu> parentList = menuService.getParentList();
        return ResultUtils.success("查询成功", parentList);
    }

    // 查询树形表格数据
    @GetMapping("/menulist")
    public ResultVo<Object> getList() {
        List<Menu> list = menuService.getList();
        return ResultUtils.success("查询成功", list);
    }
}

