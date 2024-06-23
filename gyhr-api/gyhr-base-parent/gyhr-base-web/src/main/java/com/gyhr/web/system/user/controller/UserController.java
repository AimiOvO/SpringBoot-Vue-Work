package com.gyhr.web.system.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.config.jwt.JwtUtils;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.rental.customer.entity.Customer;
import com.gyhr.web.rental.customer.service.CustomerService;
import com.gyhr.web.system.menu.entity.MakeMenuTree;
import com.gyhr.web.system.menu.entity.Menu;
import com.gyhr.web.system.menu.entity.RouterVO;
import com.gyhr.web.system.menu.service.MenuService;
import com.gyhr.web.system.user.entity.*;
import com.gyhr.web.system.user.service.UserService;
import com.gyhr.web.system.userRole.entity.UserRole;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 员工管理控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MenuService menuService;
    @Autowired
    private CustomerService customerService;

//    /**
//     * 用户登录
//     * @param parm 请求参数
//     * @return 返回用户id和token
//     */
//    @PostMapping("/login")
//    public ResultVo<Object> login(@RequestBody LoginParm parm) {
//        if (StringUtils.isEmpty(parm.getUsername())
//                || StringUtils.isEmpty(parm.getPassword())
//                || StringUtils.isEmpty(parm.getUserType())) {
//            return ResultUtils.error("用户名、密码、用户类型不能为空");
//        }
//        // 加密前端传来的密码
//        String password = DigestUtils.md5DigestAsHex(parm.getPassword().getBytes());
//        // 构造查询条件
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda()
//                .eq(User::getUsername, parm.getUsername())
//                .eq(User::getPassword, password);
//        User user = userService.getOne(queryWrapper);
//        // 如果没有查到用户
//        if (user == null) {
//            return ResultUtils.error("用户名、密码或用户类型错误！");
//        }
//        // 生成token返回前端
//        String token = jwtUtils.generateToken(user.getUsername());
//        // 获取token过期时间
//        Long expireTime = jwtUtils.getExpireTime(token);
//        LoginResult result = new LoginResult();
//        result.setUserId(user.getUserId());
//        result.setToken(token);
//        result.setExpireTime(expireTime);
//
//        return ResultUtils.success("登录成功", result);
//    }

    /**
     * 获取菜单列表
     * @param request
     * @return
     */
    @GetMapping("/getMenuList")
    public ResultVo<Object> getMenuList(HttpServletRequest request) {
        // 从头部获取token
        String token = request.getHeader("token");
        // 获取用户名
        String username = jwtUtils.getUsernameFromToken(token);
        // 获取用户类型
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Object userType = claims.get(("userType"));
        if (userType.equals("0")) { // 业主
            // 查询业主的权限信息
            Customer customer = customerService.loadCustomer(username);
            List<Menu> menuList = menuService.getMenuByUserIdForCustomer(customer.getCustomerId());
            List<Menu> collect = menuList.stream().filter(item -> item != null && !item.getType().equals("2"))
                    .collect(Collectors.toList());
            // 组装路由数据
            List<RouterVO> routerVOS = MakeMenuTree.makeRouter(collect, 0L);

            return ResultUtils.success("查询成功", routerVOS);
        } else {
            // 查询业主的权限信息
            User user = userService.loadUser(username);
            List<Menu> menuList = menuService.getMenuByUserId(user.getUserId());
            List<Menu> collect = menuList.stream().filter(item -> item != null && !item.getType().equals("2"))
                    .collect(Collectors.toList());
            // 组装路由数据
            List<RouterVO> routerVOS = MakeMenuTree.makeRouter(collect, 0L);

            return ResultUtils.success("查询成功", routerVOS);
        }
    }

    /**
     * 用户登录
     * @param parm 请求参数
     * @return 返回用户id和token
     */
    @PostMapping("/login")
    public ResultVo<Object> login(@RequestBody LoginParm parm) {
        if (StringUtils.isEmpty(parm.getUsername())
                || StringUtils.isEmpty(parm.getPassword())
                || StringUtils.isEmpty(parm.getUserType())) {
            return ResultUtils.error("用户名、密码、用户类型不能为空");
        }
        // 加密后的密码
        passwordEncoder.encode(parm.getPassword());
        // spring security需要的token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                parm.getUsername()+":"+parm.getUserType(), parm.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        if (parm.getUserType().equals("0")) { // 业主
            Customer customer = (Customer) authenticate.getPrincipal();
            String token = jwtUtils.generateToken(parm.getUsername(), parm.getUserType());
            Long expireTime = jwtUtils.getExpireTime(token);
            LoginResult result = new LoginResult();
            result.setUserId(customer.getCustomerId());
            result.setToken(token);
            result.setExpireTime(expireTime);

            return ResultUtils.success("登录成功",result);
        } else if (parm.getUserType().equals("1")) { // 业主
            User user = (User) authenticate.getPrincipal();
            String token = jwtUtils.generateToken(parm.getUsername(), parm.getUserType());
            Long expireTime = jwtUtils.getExpireTime(token);
            LoginResult result = new LoginResult();
            result.setUserId(user.getUserId());
            result.setToken(token);
            result.setExpireTime(expireTime);

            return ResultUtils.success("登录成功",result);
        } else {
            return ResultUtils.error("您选择的用户类型不存在!");
        }
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/loginOut")
    public ResultVo<Object> loginOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResultUtils.success("退出登录成功!");
    }

    @GetMapping("/getInfo")
    public ResultVo<Object> getInfo(User user, HttpServletRequest request) {
        // 从头部获取token
        String token = request.getHeader("token");
        Object userType = jwtUtils.getClaimsFromToken(token).get("userType");
        if (userType.equals("0")) { // 业主
            Customer customer = customerService.getById(user.getUserId());
            UserInfo userInfo = new UserInfo();
            userInfo.setId(customer.getCustomerId());
            userInfo.setName(customer.getCname());
            userInfo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            // 查询业主的权限信息
            List<Menu> menuList = menuService.getMenuByUserIdForCustomer(customer.getCustomerId());
            // 获取权限字段转成数组
            String[] collectArray = menuList.stream().filter(Objects::nonNull).map(Menu::getMenuCode)
                    .filter(Objects::nonNull).toArray(String[]::new);
            userInfo.setRoles(collectArray);

            return ResultUtils.success("获取用户信息成功", userInfo);
        } else { // 物管
            // 根据用户id查询
            User getUser = userService.getById(user.getUserId());
            UserInfo userInfo = new UserInfo();
            userInfo.setId(getUser.getUserId());
            userInfo.setName(getUser.getUname());
            userInfo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            // 根据用户id查询权限字段
            List<Menu> menuList = menuService.getMenuByUserId(user.getUserId());
            // 获取权限字段转成数组
            String[] collectArray = menuList.stream().filter(Objects::nonNull).map(Menu::getMenuCode)
                    .filter(Objects::nonNull).toArray(String[]::new);
            userInfo.setRoles(collectArray);

            return ResultUtils.success("获取用户信息成功", userInfo);
        }
    }

//    @PostMapping("/resetPassword")
//    public ResultVo<Object> resetPassword(@RequestBody User user, HttpServletRequest request) {
//        // 获取token 获取用户类型
//        String token = request.getHeader("token");
//        String username = jwtUtils.getUsernameFromToken(token);
//        Object userType = jwtUtils.getClaimsFromToken(token).get("userType");
//        // 判断用户类型
//        if (userType.equals("0")) { // 业主
//            Customer customer = customerService.getById(user.getUserId());
//            // 原来的密码
//            String oldPW = customer.getPassword();
//
//        } else { // 物管
//
//        }
//    }

    /**
     * 新增员工
     * @param user
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public ResultVo<Object> addUser(@RequestBody User user) {
        // 判断登录名是否唯一
        if (StringUtils.isNotEmpty(user.getUsername())) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername, user.getUsername());
            if (userService.getOne(queryWrapper) != null) {
                return ResultUtils.error("登录名已经被占用", 500);
            }
        }
        // 如果密码存在 MD5加密
        if (StringUtils.isNotEmpty(user.getPassword())) {
//            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setDelFlag("0");
        boolean save = userService.save(user);
        if (save){
            return ResultUtils.success("新增员工成功");
        }

        return ResultUtils.error("新增员工失败");
    }

    /**
     * 编辑员工
     * @param user
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public ResultVo<Object> editUser(@RequestBody User user) {
        // 判断登录名是否唯一
        if (StringUtils.isNotEmpty(user.getUsername())) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername, user.getUsername());
            User one = userService.getOne(queryWrapper);
            if (one != null && !Objects.equals(one.getUserId(), user.getUserId())) {
                return ResultUtils.error("登录名已经被占用", 500);
            }
        }
        // 如果密码存在 MD5加密
        if (StringUtils.isNotEmpty(user.getPassword())) {
//            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        boolean edit = userService.updateById(user);
        if (edit){
            return ResultUtils.success("编辑员工成功");
        }

        return ResultUtils.error("编辑员工失败");
    }

    /**
     * 删除员工
     * @param customerId
     * @return
     */
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public ResultVo<Object> deleteUser(@PathVariable("customerId") Long customerId) {
        boolean delete = userService.deleteUser(customerId);
        if (delete){
            return ResultUtils.success("删除员工成功");
        }

        return ResultUtils.error("删除员工失败");
    }

    /**
     * 查询员工列表
     * @param parm
     * @return
     */
    @GetMapping("/userlist")
    public ResultVo<Object> userlist(UserParm parm) {
        IPage<User> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        IPage<User> list = userService.getList(page, parm.getUname(), parm.getUphone(), parm.getRoleId());

        return ResultUtils.success("查询成功", list);
    }

    /**
     * 查询维修人员列表
     * @param parm
     * @return
     */
    @GetMapping("/repairTorList")
    public ResultVo<Object> repairTorList(UserParm parm){
        IPage<User> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        IPage<User> list = userService.getRepairTorList(page, parm.getUname(), parm.getUphone());

        return ResultUtils.success("查询成功", list);
    }

    /**
     * 根据用户id查询角色id
     * @param userRole 用户id
     * @return 角色id
     */
    @GetMapping("/getRoleByUserId")
    public ResultVo<Object> getRoleByUserId(UserRole userRole){
        UserRole userRole1 = userService.getRoleByUserId(userRole.getUserId());
        return ResultUtils.success("查询成功", userRole1);
    }

    /**
     * 分配角色保存
     * @param userRole
     * @return
     */
    @PostMapping("/saveRole")
    @PreAuthorize("hasAuthority('sys:user:saverole')")
    public ResultVo<Object> saveRole(@RequestBody UserRole userRole){
        userService.saveRole(userRole);
        return ResultUtils.success("分配部门成功!");
    }
}
