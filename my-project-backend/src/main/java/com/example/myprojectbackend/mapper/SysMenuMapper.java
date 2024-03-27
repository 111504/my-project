package com.example.myprojectbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myprojectbackend.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}
