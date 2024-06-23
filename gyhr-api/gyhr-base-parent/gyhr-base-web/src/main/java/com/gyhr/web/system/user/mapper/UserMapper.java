package com.gyhr.web.system.user.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.system.user.entity.User;

/**
 * 员工数据访问层
 */
public interface UserMapper extends BaseMapper<User> {
    IPage<User> getList(IPage<User> page, @Param("uname") String uname, @Param("uphone") String uphone,
        @Param("roleId") Long roleId);

    IPage<User> getRepairTorList(IPage<User> page, @Param("uname") String uname, @Param("uphone") String uphone);

}
