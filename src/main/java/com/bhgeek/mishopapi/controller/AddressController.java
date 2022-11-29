package com.bhgeek.mishopapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.UserContext;
import com.bhgeek.mishopapi.entity.Address;
import com.bhgeek.mishopapi.entity.SystemCity;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.mapper.AddressMapper;
import com.bhgeek.mishopapi.service.ISystemCityService;
import com.bhgeek.mishopapi.utils.CityTreeUtil;
import com.bhgeek.mishopapi.vo.CityVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-25
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private ISystemCityService systemCityService;

    @Autowired
    private AddressMapper addressMapper;

    @GetMapping("/citys")
    public RespBean getCityList() {
        List<SystemCity> systemCities = systemCityService.list();
        List<CityVo> cityVOS = Lists.newArrayList();

        for (SystemCity systemCity : systemCities){
            CityVo cityVO = new CityVo();
            cityVO.setValue(systemCity.getCityId());
            cityVO.setLabel(systemCity.getName());
            cityVO.setPid(systemCity.getParentId());
            cityVOS.add(cityVO);
        }
        List<CityVo> cityVos = CityTreeUtil.list2TreeConverter(cityVOS, 0);
        return new RespBean(RespBeanEnum.SUCCESS,cityVos);
    }

    @PostMapping("/add")
    @AuthCheck
    public RespBean addAddress(@RequestBody Address address){
        Integer userId = UserContext.getUserId();
        address.setUid(userId);
        if (address.getIsDefault() == 1) {
            List<Address> addresses = addressMapper.selectList(new LambdaQueryWrapper<Address>().eq(Address::getUid, userId));
            for (Address address1 : addresses) {
                address1.setIsDefault(0);
                addressMapper.updateById(address1);
            }
        }

        if (address.getId() != null) {
            addressMapper.updateById(address);
        } else {
            addressMapper.insert(address);
        }
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @GetMapping()
    @AuthCheck
    public RespBean getAddress(){
        Integer userId = UserContext.getUserId();
        List<Address> addresses = addressMapper.selectList(new LambdaQueryWrapper<Address>().eq(Address::getUid, userId));
        return new RespBean(RespBeanEnum.SUCCESS, addresses);
    }

    @PostMapping("/del")
    @AuthCheck
    public RespBean delAddress(Integer id) {
        addressMapper.deleteById(id);
        return new RespBean(RespBeanEnum.SUCCESS);
    }
}
