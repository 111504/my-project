package com.example.myprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myprojectbackend.entity.system.SysUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface SysUserService extends IService<SysUser> {
    String getUserAuthorityInfo(Long userId);


}
