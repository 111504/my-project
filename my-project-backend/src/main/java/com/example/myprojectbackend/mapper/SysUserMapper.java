package com.example.myprojectbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myprojectbackend.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
