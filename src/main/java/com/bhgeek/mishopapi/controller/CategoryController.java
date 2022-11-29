package com.bhgeek.mishopapi.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.dto.CategroyDto;
import com.bhgeek.mishopapi.entity.Category;
import com.bhgeek.mishopapi.entity.Goods;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.mapper.GoodsMapper;
import com.bhgeek.mishopapi.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private GoodsMapper goodsMapper;

    @GetMapping
    public RespBean getCategory(){
        List<Category> list = categoryService.list();
        List<CategroyDto> collect = list.stream().map(e -> {
            Integer id = e.getId();
            CategroyDto categroyDto = new CategroyDto();
            BeanUtils.copyProperties(e, categroyDto);
            List<Goods> goods = goodsMapper.selectList(Wrappers.<Goods>lambdaQuery().eq(Goods::getCid, id));
            categroyDto.setGoods(goods);
            return categroyDto;
        }).collect(Collectors.toList());
        return new RespBean(RespBeanEnum.SUCCESS,collect);
    };

    @PostMapping("/edit")
    @AuthCheck
    public RespBean editCategory(Category category) {
        categoryService.updateById(category);

        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @PostMapping("/cover")
    @AuthCheck
    public RespBean addCateCover(Integer id , String data) {
        Category category = categoryService.getById(id);
        category.setCover(data);
        categoryService.saveOrUpdate(category);
        return new RespBean(RespBeanEnum.SUCCESS);
    }
}
