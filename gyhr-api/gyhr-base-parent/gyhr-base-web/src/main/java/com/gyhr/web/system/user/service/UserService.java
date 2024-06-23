package com.gyhr.web.system.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.system.user.entity.User;
import com.gyhr.web.system.userRole.entity.UserRole;

public interface UserService extends IService<User> {

    // 查询用户列表
    IPage<User> getList(IPage<User> page, String uname, String uphone, Long roleId);
    // 查询维修人员
    IPage<User> getRepairTorList(IPage<User> page, String uname, String uphone);
    //根据用户id查询角色id
    UserRole getRoleByUserId(Long userId);
    //分配角色保存
    void saveRole(UserRole userRole);
    boolean deleteUser(Long customerId);
    User loadUser(String username);
}
