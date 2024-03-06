package com.mqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.MenuDto;
import com.mqz.domin.entity.LoginUser;
import com.mqz.domin.entity.Menu;
import com.mqz.domin.entity.Role;
import com.mqz.domin.entity.User;
import com.mqz.domin.vo.UserInfoVo;
import com.mqz.exception.SystemException;
import com.mqz.service.LoginService;
import com.mqz.service.MenuService;
import com.mqz.service.RoleService;
import com.mqz.service.UserService;
import com.mqz.utils.BeanCopyUtils;
import com.mqz.utils.JwtUtil;
import com.mqz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.mqz.constants.SystemConstants.ADMIN_LOGIN_REDIS_KEY_PREFIX;
import static com.mqz.constants.SystemConstants.BACKGROUND_PARENTID;
import static com.mqz.enums.AppHttpCodeEnum.USER_ROLE_IS_EMPTY;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断验证是否通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或者密码错误！");
        }
        //获取用户id来生成token
        LoginUser loginUser =(LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String userIdStr = userId.toString();
        String jwt = JwtUtil.createJWT(userId.toString());
        //将用户的id和用户的信息保存到redis中去
        redisCache.setCacheObject(ADMIN_LOGIN_REDIS_KEY_PREFIX+userIdStr,loginUser);
        //把token封装返回
        Map<String, String> token_content = new HashMap<>();
        token_content.put("token",jwt);
        return ResponseResult.okResult(token_content);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser)authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        String idStr = id.toString();
        //获取id后删除redis中的用户信息
        redisCache.deleteObject(ADMIN_LOGIN_REDIS_KEY_PREFIX+idStr);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getinfo() {
        //先获取用户的id
        LoginUser loginUser =(LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = loginUser.getUser().getId();
        //根据用户id查询用户所拥有的权限
        List<String> permissions = menuService.getPermissions(id);
        //查询用户的角色并放进一个数组里
        List<Long> roleIds = roleService.getRoleId(id);
        if (roleIds==null){
            throw new SystemException(USER_ROLE_IS_EMPTY);
        }
        List<Role> roleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            Role byId = roleService.getById(roleId);
            roleList.add(byId);
        }
        List<String> roleKeyList = new ArrayList<>();
        for (Role role : roleList) {
            String roleKey = role.getRoleKey();
            roleKeyList.add(roleKey);
        }
        for (String roleKey : roleKeyList) {
            if (roleKey.equals("admin")){
                LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
                menuLambdaQueryWrapper.in(Menu::getMenuType,"C","F");
                menuLambdaQueryWrapper.eq(Menu::getStatus,"0");
                menuLambdaQueryWrapper.select(Menu::getPerms);
                List<Menu> list = menuService.list(menuLambdaQueryWrapper);
                List<String> collect = list.stream().map(Menu::getPerms).collect(Collectors.toList());
                permissions.addAll(collect);
            }
        }

        Object[] objects = permissions.toArray();
        Object[] roleArray = roleKeyList.toArray();
        //封装用户信息
        User user = userService.getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("permissions",objects);
        resultMap.put("roles",roleArray);
        resultMap.put("user",userInfoVo);
        return ResponseResult.okResult(resultMap);
    }
    @Override
    public ResponseResult getRouters() {
        //先获取用户id判断用户的级别
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();
        List<Long> roleIds = roleService.getRoleId(userId);
        //判断角色ID
        if (roleIds == null) {
            throw new SystemException(USER_ROLE_IS_EMPTY);
        }
        List<MenuDto> collect = null;
        for (Long roleId : roleIds) {
            //超级管理员
            if (roleId.equals(1L)) {
                //先查父节点
                LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
                menuLambdaQueryWrapper.eq(Menu::getParentId,BACKGROUND_PARENTID);
                menuLambdaQueryWrapper.in(Menu::getMenuType, "C","M");
                menuLambdaQueryWrapper.eq(Menu::getStatus, "0");
                List<Menu> list = menuService.list(menuLambdaQueryWrapper);
                collect = list.stream().map(menu -> BeanCopyUtils.copyBean(menu, MenuDto.class)).collect(Collectors.toList());
                //再查子节点
                getChildrenMenu(collect);
            }else {
                collect = getTreeMenu(roleId);
            }
        }

        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("menus",collect);
        return ResponseResult.okResult(resultMap);
    }

    /**
     * 查询子节点
     * @param collect 父节点的集合
     */
    private void getChildrenMenu(List<MenuDto> collect) {
        for (MenuDto menu : collect) {
            long parentId = menu.getId();
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper1.eq(Menu::getParentId, parentId);
            menuLambdaQueryWrapper1.eq(Menu::getMenuType, "C");
            menuLambdaQueryWrapper1.eq(Menu::getStatus, "0");
            List<Menu> list1 = menuService.list(menuLambdaQueryWrapper1);
            List<MenuDto> collect1 = list1.stream().map(menu1 -> BeanCopyUtils.copyBean(menu1, MenuDto.class)).collect(Collectors.toList());
            MenuDto[] menuDtos = collect1.toArray(new MenuDto[collect1.size()]);
            menu.setChildren(menuDtos);
        }
    }
    private List<MenuDto> getTreeMenu(Long roleId){
        //查父节点
        List<Menu> list = menuService.getMenus(roleId);
        List<Menu> dedMenus = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            Menu menu = list.get(i);
            if (menu.getParentId()==BACKGROUND_PARENTID){
                dedMenus.add(menu);
                list.remove(menu);
            }
        }
        List<MenuDto> listMenu = dedMenus.stream().map(menu -> BeanCopyUtils.copyBean(menu, MenuDto.class)).collect(Collectors.toList());
        //再查子节点
        List<Menu> sonMenus = new ArrayList<>();
        for (MenuDto menu : listMenu) {
            for (Menu menu1 : list) {
                if(Objects.equals(menu1.getParentId(), menu.getId())){
                    sonMenus.add(menu1);
                }
            }
            List<MenuDto> collect1 = sonMenus.stream().map(menu1 -> BeanCopyUtils.copyBean(menu1, MenuDto.class)).collect(Collectors.toList());
            MenuDto[] menuDtos = collect1.toArray(new MenuDto[collect1.size()]);
            menu.setChildren(menuDtos);
        }
        return listMenu;
    }
}
