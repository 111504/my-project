package com.example.myprojectbackend.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName(value ="sys_user")
@Data
public class SysUser extends BaseEntity implements Serializable {


    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号码
     */
    @TableField(value = "phonenumber")
    private String phonenumber;

    /**
     * 最后登录时间
     */
    @TableField(value = "login_date")
    private Date loginDate;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 所属角色 多个角色 逗号隔开
     */
    @TableField(exist = false)
    private String roles;

    /**
     * 旧密码
     */
    @TableField(exist = false)
    private String oldPassword;

    /**
     * 确认新密码
     */
    @TableField(exist = false)
    private String newPassword;

    /**
     * 所有角色集合
     */
    @TableField(exist = false)
    public List<SysRole> sysRoleList;

    /**
     * 用戶的 uuid
     */
    @TableField(value = "uuid")
    public String uuid;

}