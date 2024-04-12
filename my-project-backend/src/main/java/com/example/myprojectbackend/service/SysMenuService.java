package com.example.myprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myprojectbackend.entity.system.SysMenu;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList);
}