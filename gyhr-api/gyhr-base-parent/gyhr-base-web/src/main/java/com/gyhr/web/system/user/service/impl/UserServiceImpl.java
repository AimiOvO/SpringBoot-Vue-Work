package com.gyhr.web.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.system.user.entity.User;
import com.gyhr.web.system.user.mapper.UserMapper;
import com.gyhr.web.system.user.service.UserService;
import com.gyhr.web.system.userRole.entity.UserRole;
import com.gyhr.web.system.userRole.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public IPage<User> getList(IPage<User> page, String uname, String uphone, Long roleId) {
        return this.baseMapper.getList(page, uname, uphone, roleId);
    }

    @Override
    public IPage<User> getRepairTorList(IPage<User> page, String uname, String uphone) {
        return this.baseMapper.getRepairTorList(page, uname, uphone);
    }

    @Override
    public UserRole getRoleByUserId(Long userId) {
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.lambda().eq(UserRole::getUserId, userId);
        return userRoleMapper.selectOne(query);
    }

    @Override
    @Transactional
    public void saveRole(UserRole userRole) {
        // 删除原来的角色
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.lambda().eq(UserRole::getUserId, userRole.getUserId());
        userRoleMapper.delete(query);
        // 保存新的角色
        userRoleMapper.insert(userRole);
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        // 假删设置用户信息
        User user = new User();
        user.setUserId(userId);
        user.setEnabled(false);
        user.setDelFlag("1");
        // 删除用户
        int update = this.baseMapper.updateById(user);
        // int delete = this.baseMapper.deleteById(userId);


        return update > 0;
    }

    @Override
    public User loadUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);

        return this.baseMapper.selectOne(queryWrapper);
    }
}
