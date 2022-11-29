package com.bhgeek.mishopapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bhgeek.mishopapi.entity.Goods;
import com.bhgeek.mishopapi.entity.GoodsAttrResult;
import com.bhgeek.mishopapi.entity.GoodsAttrValue;
import com.bhgeek.mishopapi.mapper.GoodsAttrResultMapper;
import com.bhgeek.mishopapi.mapper.GoodsAttrValueMapper;
import com.bhgeek.mishopapi.mapper.GoodsMapper;
import com.bhgeek.mishopapi.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bhgeek.mishopapi.vo.GoodsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-24
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsAttrResultMapper goodsAttrResultMapper;

    @Autowired
    private GoodsAttrValueMapper goodsAttrValueMapper;


    //得到32位的uuid
    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    @Override
    @Transactional
    public void addGoods(GoodsVo vo) {
        Goods goods = new Goods();
        goods.setCid(vo.getCid());
        goods.setStoreName(vo.getStoreName());
        goods.setStoreInfo(vo.getStoreInfo());
        goods.setImage(vo.getImage());
        goods.setImageArr(vo.getImageArr());
        goods.setKeyword(vo.getKeyword());
        goods.setPrice(new BigDecimal(vo.getPrice()));
        goods.setDescription(vo.getDescribe());
        goods.setStock(vo.getStock());
        goods.setSale(0);
        goods.setStatus(1);
        goods.setIsDelete(0);
        goodsMapper.insert(goods);


        JSONObject objects = JSONObject.parseObject(vo.getSkuInfo());
        JSONArray tableColumnList = (JSONArray) objects.get("tableBodyList");

        for (int i = 0; i < tableColumnList.size(); i++) {
            GoodsAttrValue attrValue = new GoodsAttrValue();
            JSONObject object = (JSONObject) tableColumnList.get(i);
            String goodsPrice = object.getString("goodsPrice");
            String sku = object.getString("sku");
            Integer goodsStock = Integer.valueOf(object.getString("goodsStock"));
            String unique = getUUID32();

            attrValue.setSale(0);
            attrValue.setSku(sku);
            BigDecimal price = new BigDecimal(goodsPrice);
            attrValue.setPrice(price);
            attrValue.setStock(goodsStock);
            attrValue.setImage(vo.getImage());
            attrValue.setUnique(unique);
            attrValue.setPid(goods.getId());
            goodsAttrValueMapper.insert(attrValue);
        }

        GoodsAttrResult attrResult = new GoodsAttrResult();
        attrResult.setPid(goods.getId());
        attrResult.setAttrValue(vo.getSkuInfo());
        goodsAttrResultMapper.insert(attrResult);
    }

    @Override
    public Page<Goods> getGoods(String keyword, Integer page, Integer size , Integer isDelete) {
        Page<Goods> goodsPage = new Page<>(page, size);

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.like(Goods::getKeyword,keyword);
        }
        queryWrapper.eq(Goods::getIsDelete,isDelete);
        Page<Goods> result = page(goodsPage, queryWrapper);

        return result;
    }
}
