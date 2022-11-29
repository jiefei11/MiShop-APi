package com.bhgeek.mishopapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bhgeek.mishopapi.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
