package com.example.myprojectbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.system.SysMenu;
import com.example.myprojectbackend.service.SysMenuService;
import com.example.myprojectbackend.service.SysUserService;
import com.example.myprojectbackend.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询所有菜單樹
     *
     * @return
     */
    @RequestMapping("/treeList")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public RestBean<Map<String, List<SysMenu>>> treeList() {
        List<SysMenu> menuList = sysMenuService.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));
        //回傳一個內部儲存SysMenu的list
        Map<String, List<SysMenu>> testingMapObject = new HashMap<>();
        testingMapObject.put("treeMenu", sysMenuService.buildTreeMenu(menuList));

        return RestBean.success(testingMapObject);
    }


    /**
     * 添加或者修改
     *
     * @param sysMenu
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:menu:add')" + "||" + "hasAuthority('system:menu:edit')")
    public RestBean<Void> save(@RequestBody SysMenu sysMenu) {
        if (sysMenu.getId() == null || sysMenu.getId() == -1) {
            //要清空id，否則存進資料庫時id為負數
            sysMenu.setId(null);
            sysMenu.setCreateTime(new Date());
            if(sysMenu.getMenuType().equals("C")){
                String modifiedString=StringUtil.modifyString(sysMenu.getComponent(),sysMenu.getMenuType());
                sysMenu.setPath(modifiedString);
            }
            /*開始處理目錄
              1.parent_id 設定為 0
              2.找尋目前資料庫中(在parent_id為0的前提下)找尋order_num的最大值
              3.設定path
              4.不需要設定 component
            */
            else if(sysMenu.getMenuType().equals("M")){
                sysMenu.setParentId(0L);
                Integer maxOrderNum=sysUserService.findMaxOrderNum();
                sysMenu.setOrderNum(maxOrderNum+1);

                String modifiedString=StringUtil.modifyString(sysMenu.getComponent(),sysMenu.getMenuType());
                //前端傳入component 賦值給path 清空 components
                sysMenu.setPath(modifiedString);
                sysMenu.setComponent("");
            }

            sysMenuService.save(sysMenu);
        } else {
            String modifiedString=StringUtil.modifyString(sysMenu.getComponent(),sysMenu.getMenuType());
            sysMenu.setPath(modifiedString);
            sysMenu.setUpdateTime(new Date());
            sysMenuService.updateById(sysMenu);
        }
        return RestBean.success();
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public RestBean<Map<String, Object>> findById(@PathVariable(value = "id") Long id) {

        if (sysMenuService.getById(id) != null) {
            SysMenu sysMenu = sysMenuService.getById(id);
            Map<String, Object> map = new HashMap<>();
            map.put("sysMenu", sysMenu);
            return RestBean.success(map);
        } else {
            return RestBean.failure(500, "查詢失敗");
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public RestBean<Void> delete(@PathVariable(value = "id") Long id) {
        //當你要刪除主功能時 必須先把該主功能旗下所包含的所有子功能刪除
        if (sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id)) > 0) {
            return RestBean.failure(500, "請先刪除子菜單！");
        } else {
            sysMenuService.removeById(id);
            return RestBean.success();
        }

    }
}