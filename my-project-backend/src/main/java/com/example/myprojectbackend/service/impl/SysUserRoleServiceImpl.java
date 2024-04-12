package com.example.myprojectbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myprojectbackend.entity.system.SysUserRole;
import com.example.myprojectbackend.mapper.SysUserRoleMapper;
import com.example.myprojectbackend.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
        implements SysUserRoleService {

}