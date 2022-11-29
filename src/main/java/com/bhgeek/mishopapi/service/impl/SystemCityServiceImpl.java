package com.bhgeek.mishopapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bhgeek.mishopapi.entity.SystemCity;
import com.bhgeek.mishopapi.mapper.SystemCityMapper;
import com.bhgeek.mishopapi.service.ISystemCityService;
import org.springframework.stereotype.Service;

@Service
public class SystemCityServiceImpl extends ServiceImpl<SystemCityMapper, SystemCity> implements ISystemCityService {
}
