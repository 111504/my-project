package com.example.myprojectbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myprojectbackend.entity.dao.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
