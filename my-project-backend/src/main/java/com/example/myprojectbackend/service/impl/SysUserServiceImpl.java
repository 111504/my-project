package com.example.myprojectbackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myprojectbackend.entity.system.SysMenu;
import com.example.myprojectbackend.entity.system.SysRole;
import com.example.myprojectbackend.entity.system.SysUser;
import com.example.myprojectbackend.mapper.SysMenuMapper;
import com.example.myprojectbackend.mapper.SysRoleMapper;
import com.example.myprojectbackend.mapper.SysUserMapper;
import com.example.myprojectbackend.service.SysUserService;
import com.example.myprojectbackend.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysMenuMapper sysMenuMapper;




    @Override
    public String getUserAuthorityInfo(Long userId) {
        StringBuffer authority=new StringBuffer();
        // 根据用户id获取所有的角色信息
        List<SysRole> roleList =
                sysRoleMapper.selectList(new QueryWrapper<SysRole>()
                        .inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + userId));
        //透過useId 查詢使用者角色對應表sys_user_role  這表把使用者身份與角色身分做關聯 一個使用者可以擁有多的角色
        //格式為[SysRole(name=普通角色, code=common), SysRole(name=测试角色, code=test)]

        if(roleList.size()>0){
            String roleCodeStrs = roleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
            authority.append(roleCodeStrs);
        }
        //處理完後格式變成ROLE_common,ROLE_test
        System.out.println("roleList="+roleList);

        // 遍历所有的角色，获取所有菜单权限 而且不重复
        Set<String> menuCodeSet=new HashSet<>();
        for(SysRole sysRole:roleList){
            //從角色列表中一一提取使用者所代表的角色id ，透過角色id遍歷 提取出該id擁有的使用者菜單
            List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
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
        if(menuCodeSet.size()>0){
            authority.append(",");
            String menuCodeStrs = menuCodeSet.stream().collect(Collectors.joining(","));
            authority.append(menuCodeStrs);
        }
        System.out.println("authority="+authority.toString());
        return authority.toString();
    }



}
