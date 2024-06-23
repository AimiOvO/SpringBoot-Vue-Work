package com.gyhr.config.security.service;

import com.gyhr.web.rental.customer.entity.Customer;
import com.gyhr.web.rental.customer.service.CustomerService;
import com.gyhr.web.system.menu.entity.Menu;
import com.gyhr.web.system.menu.service.MenuService;
import com.gyhr.web.system.user.entity.User;
import com.gyhr.web.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 自定义UserDetailsService
 * spring security查询用户信息
 */
@Component("CustomUserDetailService") // 交给springboot管理
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 获取用户类型
        // s= "username:1"
        int index = s.indexOf(":");
        String username = s.substring(0, index);
        String userType = s.substring(index+1, s.length());
//        UserDetails user = null;
        if (userType.equals("0")) { //业主
            Customer customer = customerService.loadCustomer(username);
            if (customer == null) {
                throw new UsernameNotFoundException("用户名或密码错误!");
            }
            // 查询业主的权限信息
            List<Menu> menuList = menuService.getMenuByUserIdForCustomer(customer.getCustomerId());
            // 获取权限字段转成数组
            String[] collectArray = menuList.stream().filter(Objects::nonNull).map(Menu::getMenuCode)
                    .filter(Objects::nonNull).toArray(String[]::new);
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(collectArray);
            // 设置用户权限
            customer.setAuthorities(authorityList);
            // 授权
            return customer;
        } else if (userType.equals("1")) { //物主
            User user =  userService.loadUser(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名或密码错误!");
            }
            // 查询用户权限信息
            List<Menu> menuList = menuService.getMenuByUserId(user.getUserId());
            // 获取权限字段转成数组
            String[] collectArray = menuList.stream().filter(Objects::nonNull).map(Menu::getMenuCode)
                    .filter(Objects::nonNull).toArray(String[]::new);
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(collectArray);
            // 设置用户权限
            user.setAuthorities(authorityList);
            // 授权
            return user;
        } else {
            throw new UsernameNotFoundException("用户类型不存在!");
        }

    }
}
