package com.example.myprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myprojectbackend.entity.system.SysMenu;
import com.example.myprojectbackend.entity.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SysUserService extends IService<SysUser> {
//    String getUserAuthorityInfo(Long userId);

    SysUser getByUsername(String username);
    List<SysMenu> requestUserInformation(Long userId);

    List<GrantedAuthority> getUserAuthority(Long userId);

    Integer findMaxOrderNum();

}
