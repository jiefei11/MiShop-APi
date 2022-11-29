package com.bhgeek.mishopapi.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.UserContext;
import com.bhgeek.mishopapi.entity.*;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.mapper.GoodsFavoriteMapper;
import com.bhgeek.mishopapi.service.ICategoryService;
import com.bhgeek.mishopapi.service.IGoodsAttrResultService;
import com.bhgeek.mishopapi.service.IGoodsAttrValueService;
import com.bhgeek.mishopapi.service.IGoodsService;
import com.bhgeek.mishopapi.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-24
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IGoodsAttrResultService goodsAttrResultService;

    @Autowired
    private IGoodsAttrValueService goodsAttrValueService;

    @Autowired
    private GoodsFavoriteMapper favoriteMapper;

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/addGoods")
    @AuthCheck
    public RespBean addGoods(@RequestBody GoodsVo vo) {
        goodsService.addGoods(vo);
        return new RespBean(RespBeanEnum.SUCCESS, "添加成功");
    }

    @GetMapping("/getGoods")
    public RespBean getGoods(String keyword, Integer page, Integer size, Integer isDelete) {
        Page<Goods> goods = goodsService.getGoods(keyword, page, size, isDelete);
        return new RespBean(RespBeanEnum.SUCCESS, goods);
    }

    @GetMapping("/getGoodsByCid")
    public RespBean getGoodsByCid(Integer id) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getCid, id).eq(Goods::getIsDelete, 0).eq(Goods::getStatus, 1);
        List<Goods> goodsList = goodsService.list(queryWrapper);
        Category category = categoryService.getById(id);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("goodsList", goodsList);
        map.put("cover", category.getCover());
        return new RespBean(RespBeanEnum.SUCCESS, map);
    }

    @PostMapping("/delGoods")
    @AuthCheck
    public RespBean delGoods(Integer id) {
        Goods goods = goodsService.getById(id);
        goods.setIsDelete(1);
        goods.setStatus(0);
        goodsService.saveOrUpdate(goods);
        return new RespBean(RespBeanEnum.SUCCESS);
    }


    @PostMapping("/restoreGoods")
    @AuthCheck
    public RespBean restoreGoods(Integer id) {
        Goods goods = goodsService.getById(id);
        goods.setIsDelete(0);
        goods.setStatus(1);
        goodsService.saveOrUpdate(goods);
        return new RespBean(RespBeanEnum.SUCCESS);
    }


    @PostMapping("/deleteCompletely")
    @AuthCheck
    public RespBean deleteCompletely(Integer id) {
        goodsService.removeById(id);
        goodsAttrResultService.remove(new LambdaQueryWrapper<GoodsAttrResult>().eq(GoodsAttrResult::getPid, id));
        goodsAttrValueService.remove(new LambdaQueryWrapper<GoodsAttrValue>().eq(GoodsAttrValue::getPid, id));
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @GetMapping("/productDetail")
    public RespBean getProductDetail(Integer id) {
        HashMap<Object, Object> map = new HashMap<>();
        Goods goods = goodsService.getById(id);
        map.put("storeInfo", goods);
        GoodsAttrResult one = goodsAttrResultService.getOne(new LambdaQueryWrapper<GoodsAttrResult>().eq(GoodsAttrResult::getPid, id));
        String value = one.getAttrValue();
        JSONObject object = JSONObject.parseObject(value);
        JSONArray attrInfo = (JSONArray) object.get("attrInfo");
        List<GoodsAttrValue> list = goodsAttrValueService.list(new LambdaQueryWrapper<GoodsAttrValue>().eq(GoodsAttrValue::getPid, id));
        map.put("attrInfo", attrInfo);
        map.put("attrValue", list);
        try {
            GoodsFavorite favorite = favoriteMapper.selectOne(Wrappers.<GoodsFavorite>lambdaQuery().eq(GoodsFavorite::getProductId, id).eq(GoodsFavorite::getUid, UserContext.getUserId()));
            if (favorite != null) {
                map.put("favorite", true);
            } else {
                map.put("favorite", false);
            }
        } catch (Exception e) {

        }
        return new RespBean(RespBeanEnum.SUCCESS, map);
    }

    @PostMapping("/offShelf")
    @AuthCheck
    public RespBean offShelf(Integer id) {
        Goods good = goodsService.getById(id);
        Integer status = good.getStatus();
        if (status == 1) {
            good.setStatus(0);
        } else {
            good.setStatus(1);
        }
        goodsService.updateById(good);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @PostMapping("/addFavorite")
    @AuthCheck
    public RespBean addFavorite(Integer id) {
        Integer userId = UserContext.getUserId();
        GoodsFavorite one = favoriteMapper.selectOne(Wrappers.<GoodsFavorite>lambdaQuery().eq(GoodsFavorite::getUid, userId).eq(GoodsFavorite::getProductId, id));
        if (one == null) {
            GoodsFavorite goodsFavorite = new GoodsFavorite();
            goodsFavorite.setUid(userId);
            goodsFavorite.setProductId(id);
            favoriteMapper.insert(goodsFavorite);
        } else {
            favoriteMapper.deleteById(one);
        }
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @GetMapping("/getFavorite")
    @AuthCheck
    public RespBean getFavorite() {
        Integer userId = UserContext.getUserId();
        List<GoodsFavorite> goodsFavorites = favoriteMapper.selectList(Wrappers.lambdaQuery(GoodsFavorite.class).eq(GoodsFavorite::getUid, userId));
        List<Goods> collect = goodsFavorites.stream().map(e -> {
            Goods goods = goodsService.getById(e.getProductId());
            return goods;
        }).collect(Collectors.toList());

        return new RespBean(RespBeanEnum.SUCCESS, collect);
    }

}
