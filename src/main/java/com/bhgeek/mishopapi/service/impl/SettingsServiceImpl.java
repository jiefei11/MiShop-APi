package com.bhgeek.mishopapi.service.impl;

import com.bhgeek.mishopapi.entity.Settings;
import com.bhgeek.mishopapi.mapper.SettingsMapper;
import com.bhgeek.mishopapi.service.ISettingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-24
 */
@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsMapper, Settings> implements ISettingsService {

}
