package com.example.myprojectbackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myprojectbackend.entity.system.SysMenu;
import com.example.myprojectbackend.entity.system.SysRole;
import com.example.myprojectbackend.entity.system.SysUser;
import com.example.myprojectbackend.mapper.SysUserMapper;
import com.example.myprojectbackend.service.SysMenuService;
import com.example.myprojectbackend.service.SysRoleService;
import com.example.myprojectbackend.service.SysUserService;
import com.example.myprojectbackend.utils.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Resource
    SysRoleService  sysRoleService;

    @Resource
    SysMenuService sysMenuService;



    @Override
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        //  格式ROLE_admin,ROLE_common,system:user:resetPwd,system:role:delete,system:user:list,system:menu:query,system:menu:list,system:menu:add,system:user:delete,system:role:list,system:role:menu,system:user:edit,system:user:query,system:role:edit,system:user:add,system:user:role,system:menu:delete,system:role:add,system:role:query,system:menu:edit
        String authority=getUserAuthorityInfo(userId);
        System.out.println("authority="+authority);
        //AuthorityUtils.commaSeparatedStringToAuthorityList 是 Spring Security 提供的一个工具方法，用于将逗号分隔的权限字符串转换成一个 List<GrantedAuthority>
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }


    public String getUserAuthorityInfo(Long userId) {
        StringBuilder authority=new StringBuilder();
        // 根据用户id获取所有的角色信息
        List<SysRole> roleList =
                sysRoleService.list(new QueryWrapper<SysRole>()
                        .inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + userId));
        //透過useId 查詢使用者角色對應表sys_user_role  這表把使用者身份與角色身分做關聯 一個使用者可以擁有多的角色
        //格式為[SysRole(name=普通角色, code=common), SysRole(name=测试角色, code=test)]

        if(!roleList.isEmpty()){
            String roleCodeStrs = roleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
            authority.append(roleCodeStrs);
        }
        //處理完後格式變成ROLE_common,ROLE_test
        System.out.println("roleList="+roleList);

        // 遍历所有的角色，获取所有菜单权限 而且不重复
        Set<String> menuCodeSet=new HashSet<>();
        for(SysRole sysRole:roleList){
            //從角色列表中一一提取使用者所代表的角色id ，透過角色id遍歷 提取出該id擁有的使用者菜單
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
            System.out.println("sysMenuList="+sysMenuList);
            //把權限標示加入
            for(SysMenu sysMenu:sysMenuList){
                String perms=sysMenu.getPerms();
                if(StringUtil.isNotEmpty(perms)){
                    menuCodeSet.add(perms);
                }
            }
        }
        System.out.println("menuCodeSet="+menuCodeSet);

       //把menuCodeSet內容根據逗號隔開 menuCodeSet=[system:user:list, system:menu:list, system:role:list]
        if(!menuCodeSet.isEmpty()){
            authority.append(",");
            String menuCodeStrs = String.join(",", menuCodeSet);
            authority.append(menuCodeStrs);
        }
        System.out.println("authority="+authority.toString());
        return authority.toString();
    }

    @Override
    public List<SysMenu> requestUserInformation(Long userId) {
        // 根据用户id获取所有的角色信息
        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + userId));

        // 遍历所有的角色，获取所有菜单权限 而且不重复(由於不重複這個特性所以使用Set)
        Set<SysMenu> menuSet=new HashSet<>();
        for(SysRole sysRole:roleList){
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
            for(SysMenu sysMenu:sysMenuList){
                menuSet.add(sysMenu);
            }
        }

        List<SysMenu> sysMenuList=new ArrayList<>(menuSet);

        // 排序 每個功能會記錄父節點，並且每一個節點記錄在同一個父節點底下，各節點的排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));
        // 转菜单树
        List<SysMenu> menuList=sysMenuService.buildTreeMenu(sysMenuList);

        System.out.println("菜單樹排序後="+menuList.toString());



        return menuList;
    }

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    @Override
    public Integer findMaxOrderNum() {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        // 设置查询条件，parent_id为0
        queryWrapper.eq("parent_id", 0);
        // 选择查询order_num字段
        queryWrapper.select("max(order_num) as maxOrderNum");

        // 使用selectObjs方法获取一个对象列表
        List<Object> result = sysMenuService.listObjs(queryWrapper);
        System.out.println("result: " + result);
        // 检查结果是否为空并返回第一个对象，这里使用Java 8的Optional类来优雅地处理可能的null值
        return result.isEmpty() ? 0 : Integer.parseInt(result.get(0).toString());
    }


}
