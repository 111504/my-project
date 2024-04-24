package com.example.myprojectbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myprojectbackend.common.Constant;
import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.system.PageBean;
import com.example.myprojectbackend.entity.system.SysRole;
import com.example.myprojectbackend.entity.system.SysUser;
import com.example.myprojectbackend.entity.system.SysUserRole;
import com.example.myprojectbackend.service.SysRoleService;
import com.example.myprojectbackend.service.SysUserRoleService;
import com.example.myprojectbackend.service.SysUserService;
import com.example.myprojectbackend.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping("api/user")
public class SysUserController {


    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @PostMapping("/list")
    public String listUser(@RequestBody PageBean pageBean) {
        String query=pageBean.getQuery().trim();
        //回傳所有
        Page<SysUser> pageResult=sysUserService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<SysUser>().like(StringUtil.isNotEmpty(query),"username",query));

        List<SysUser> userList=pageResult.getRecords();
        for(SysUser user : userList){
            List<SysRole> roleList=sysRoleService.list(new QueryWrapper<SysRole>().inSql("id","select role_id from sys_user_role where user_id="+user.getId()));
            user.setSysRoleList(roleList);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("userList",userList);
        resultMap.put("total",pageResult.getTotal());
        return RestBean.success(resultMap).asJsonString();
    }
    /*狀態修改*/

    @GetMapping("/updateStatus/{id}/status/{status}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public RestBean<Void> updateStatus(@PathVariable("id") Integer id,
                                   @PathVariable("status")  String status) {
        SysUser sysUser = sysUserService.getById(id);
        sysUser.setUpdateTime(new Date());
        sysUser.setStatus(status);
        if (sysUserService.saveOrUpdate(sysUser)) {
            return RestBean.success(null, "狀態修改成功!");
        } else {
            return RestBean.failure(500, "狀態修改失敗");
        }


    }

    /**
     * 驗證用戶名稱是否重複
     * @param sysUser
     * @return
     */
    @PostMapping("/checkUserName")
    @PreAuthorize("hasAuthority('system:user:query')")
    public RestBean<Void> checkUserName(@RequestBody SysUser sysUser){
        if(sysUserService.getByUsername(sysUser.getUsername())==null){
            return RestBean.success();
        }else{
            return RestBean.failure(500,"錯誤，用戶名已重複");
        }
    }
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public RestBean<Map<String, Object>> findById(@PathVariable(value = "id")Integer id){
        SysUser sysUser = sysUserService.getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("sysUser",sysUser);
        return RestBean.success(map);
    }


    /**
     * 添加或者修改
     * @param sysUser
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:user:add')"+"||"+"hasAuthority('system:user:edit')")
    public RestBean<Void> save(@RequestBody SysUser sysUser){
        System.out.println("先印出帳號所有內容"+sysUser.getId()+sysUser.getUpdateTime()+sysUser.getCreateTime());



        SysUser sysUserToSave;
        if(sysUser.getId()==null || sysUser.getId()==-1){
            System.out.println("創建帳號");
            sysUserToSave = new SysUser();
            // 将请求体中的所有属性复制到新实例，除了id
            BeanUtils.copyProperties(sysUser, sysUserToSave, "id");
            // 设置创建时间和加密密码
            sysUserToSave.setCreateTime(new Date());
            sysUserToSave.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
            //創立uuid
            sysUserToSave.setUuid(StringUtil.genurateUserId());
            // 保存新创建的用户
            sysUserService.save(sysUserToSave);
//            sysUser.setCreateTime(new Date());
//            sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
//            sysUserService.save(sysUser);
        }else{
            System.out.println("更新帳號資訊 帳號創建時間"+sysUser.getCreateTime());
            sysUser.setUpdateTime(new Date());
            sysUserService.updateById(sysUser);
        }
        return RestBean.success();
    }
    /**
     * 删除
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public RestBean<Void> delete(@RequestBody Long[] ids){
        //刪除用戶表  用戶角色關聯表
        if(sysUserService.removeByIds(Arrays.asList(ids))&&sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id",  ids))){
            return RestBean.success();
        }
        else{
            return RestBean.failure(500, "刪除失敗");
        }

    }

/*重置密碼*/
    @GetMapping("/resetPassword/{id}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public RestBean<Void> resetPassword(@PathVariable(value = "id")Integer id){
        //回傳entity
        SysUser sysUser = sysUserService.getById(id.longValue());
        sysUser.setPassword(bCryptPasswordEncoder.encode(Constant.DEFAULT_PASSWORD));
        sysUser.setUpdateTime(new Date());
        if(sysUserService.updateById(sysUser)){
            return RestBean.success();
        }else{
            return RestBean.failure(500,"密码重置有误,请联系管理员!");
        }
    }

    /**
     * 角色授權
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional
    @PostMapping("/grantRole/{userId}")
    @PreAuthorize("hasAuthority('system:user:role')")
    public RestBean<Void> grantRole(@PathVariable("userId") Long userId,@RequestBody Long[] roleIds){
        List<SysUserRole> userRoleList=new ArrayList<>();
        Arrays.stream(roleIds).forEach(r -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(r);
            sysUserRole.setUserId(userId);
            userRoleList.add(sysUserRole);
        });
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        sysUserRoleService.saveBatch(userRoleList);
        return RestBean.success();
    }
    /**
     * 修改密码
     * @param sysUser
     * @return
     */
    @PostMapping("/updateUserPwd")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public RestBean<Void> updateUserPwd(@RequestBody SysUser sysUser){
        SysUser currentUser = sysUserService.getById(sysUser.getId());
        if(bCryptPasswordEncoder.matches(sysUser.getOldPassword(),currentUser.getPassword())){
            currentUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getNewPassword()));
            currentUser.setUpdateTime(new Date());
            sysUserService.updateById(currentUser);
            return RestBean.success();
        }else{
            return RestBean.failure(404,"輸入舊密碼錯誤！");
        }
    }
    @PostMapping("/test")
    public String listUserTest() {


        return RestBean.success("listUserTest").asJsonString();
    }


    @GetMapping("/test2")
    public String listUserTest2() {


        return "test successfully";
    }

}
