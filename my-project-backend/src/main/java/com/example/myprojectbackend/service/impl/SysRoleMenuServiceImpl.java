package com.example.myprojectbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myprojectbackend.entity.system.SysRoleMenu;
import com.example.myprojectbackend.mapper.SysRoleMenuMapper;
import com.example.myprojectbackend.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
        implements SysRoleMenuService {

}