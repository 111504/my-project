package com.example.myprojectbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myprojectbackend.entity.system.SysMenu;
import com.example.myprojectbackend.entity.system.SysRole;
import com.example.myprojectbackend.mapper.SysMenuMapper;
import com.example.myprojectbackend.service.SysMenuService;
import com.example.myprojectbackend.service.SysRoleService;
import com.example.myprojectbackend.utils.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Override
    public List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        List<SysMenu> resultMenuList=new ArrayList<>();

        for(SysMenu sysMenu:sysMenuList){

            // 寻找子节点
            for(SysMenu e:sysMenuList){

                //如果子節點儲存的父節點為自己，也就是找尋自身的子節點
                if(Objects.equals(e.getParentId(), sysMenu.getId())){
                    sysMenu.getChildren().add(e);
                }
            }

            if(sysMenu.getParentId()==0L){
                resultMenuList.add(sysMenu);
            }
        }

        return resultMenuList;
    }


}
