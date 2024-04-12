package com.example.myprojectbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.system.PageBean;
import com.example.myprojectbackend.entity.system.SysRole;
import com.example.myprojectbackend.entity.system.SysRoleMenu;
import com.example.myprojectbackend.entity.system.SysUserRole;
import com.example.myprojectbackend.service.SysRoleMenuService;
import com.example.myprojectbackend.service.SysRoleService;
import com.example.myprojectbackend.service.SysUserRoleService;
import com.example.myprojectbackend.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;


    @GetMapping("/listAll")
    @PreAuthorize("hasAuthority('system:role:query')")
    public RestBean<Map<String, Object>> listAll(){
        Map<String,Object> resultMap=new HashMap<>();
        List<SysRole> roleList = sysRoleService.list();
        resultMap.put("roleList",roleList);
        return RestBean.success(resultMap);
    }
    /**
     * 根据条件分页查询角色信息
     *
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('system:role:query')")
    public RestBean<Map<String, Object>> list(@RequestBody PageBean pageBean){
        String query=pageBean.getQuery().trim();
        Page<SysRole> pageResult = sysRoleService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<SysRole>().like(StringUtil.isNotEmpty(query),"name",query));
        List<SysRole> roleList = pageResult.getRecords();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("roleList",roleList);
        resultMap.put("total",pageResult.getTotal());

        return  RestBean.success(resultMap);
    }

    /**
     * 添加或者修改
     * @param sysRole
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:role:add')"+"||"+"hasAuthority('system:role:edit')")
    public RestBean<Void> save(@RequestBody SysRole sysRole){
        SysRole sysNewRole = new SysRole();

        if(sysRole.getId()==null || sysRole.getId()==-1){
            sysNewRole.setName(sysRole.getName());
            sysNewRole.setCode(sysRole.getCode());
            sysNewRole.setCreateTime(new Date());
           // sysRole.setCreateTime(new Date());
            sysRoleService.save(sysNewRole);
        }else{
            sysRole.setUpdateTime(new Date());
            sysRoleService.updateById(sysRole);
        }
        return  RestBean.success();
    }

    /**
     * 根據id查詢
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public RestBean<Map<String, Object>> findById(@PathVariable(value = "id")Integer id){
        SysRole sysRole = sysRoleService.getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("sysRole",sysRole);
        return  RestBean.success(map);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public RestBean<Void> delete(@RequestBody Long[] ids){
        sysRoleService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id",ids));
        return RestBean.success();
    }

    /**
     * 獲取當前角色的權限菜單集合
     *
     * @param id
     * @return
     */
    @GetMapping("/menus/{id}")
    @PreAuthorize("hasAuthority('system:role:menu')")
    public RestBean<List<Long>> menus(@PathVariable(value = "id")Integer id){
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        //回傳該用戶的所有menu_id
        List<Long> menuIdList = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        System.out.println("menuIdList"+menuIdList);
        return  RestBean.success(menuIdList);
    }

    /**
     * 更新角色權限訊息
     * @param id
     * @param menuIds
     * @return
     */
    @Transactional
    @PostMapping("/updateMenus/{id}")
    @PreAuthorize("hasAuthority('system:role:menu')")
    public RestBean<Void> updateMenus(@PathVariable(value = "id")Long id, @RequestBody Long[] menuIds){
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id",id));
        List<SysRoleMenu> sysRoleMenuList=new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId->{
            SysRoleMenu roleMenu=new SysRoleMenu();
            roleMenu.setRoleId(id);
            roleMenu.setMenuId(menuId);
            sysRoleMenuList.add(roleMenu);
        });
        sysRoleMenuService.saveBatch(sysRoleMenuList);
        return RestBean.success();
    }
}
