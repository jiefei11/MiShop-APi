package com.bhgeek.mishopapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.SettingEnum;
import com.bhgeek.mishopapi.entity.Settings;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.service.ISettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-24
 */
@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private ISettingsService settingsService;


    @PostMapping("/uptBanner")
    @AuthCheck
    public RespBean uptBanner(String images) {
        Settings settings = new Settings();
        settings.setSystemKey(String.valueOf(SettingEnum.MISHOP_BANNER));
        settings.setSystemValue(images);
        LambdaQueryWrapper<Settings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Settings::getSystemKey,SettingEnum.MISHOP_BANNER);
        settingsService.saveOrUpdate(settings,queryWrapper);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @GetMapping("/getBanner")
    public RespBean getBanner() {
        LambdaQueryWrapper<Settings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Settings::getSystemKey,SettingEnum.MISHOP_BANNER);
        Settings one = settingsService.getOne(queryWrapper);
        String[] images = one.getSystemValue().split(",");
        return new RespBean(RespBeanEnum.SUCCESS,images);
    }
}
