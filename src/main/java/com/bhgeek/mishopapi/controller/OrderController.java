package com.bhgeek.mishopapi.controller;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.UserContext;
import com.bhgeek.mishopapi.dto.CartDto;
import com.bhgeek.mishopapi.dto.GoodsAttrValueDto;
import com.bhgeek.mishopapi.dto.OrderInfoDto;
import com.bhgeek.mishopapi.entity.*;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.mapper.*;
import com.bhgeek.mishopapi.service.IOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-26
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private GoodsAttrValueMapper goodsAttrValueMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserMapper userMapper;


    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    @PostMapping("/getOrderInfo")
    @AuthCheck
    public RespBean getOrderInfo(String cartIds) {
        String[] id = cartIds.split(",");
        ArrayList<CartDto> cartDtos = new ArrayList<>();
        for (String s : id) {
            CartDto cartDto = new CartDto();
            Cart cart = cartMapper.selectById(s);
            Goods good = goodsMapper.selectById(cart.getProductId());
            GoodsAttrValue attrValue = goodsAttrValueMapper.selectOne(new LambdaQueryWrapper<GoodsAttrValue>().eq(GoodsAttrValue::getUnique, cart.getAttrUnique()));
            cartDto.setStoreInfo(good);
            cartDto.setAttrInfo(attrValue);
            cartDto.setCartNum(cart.getCartNum());
            cartDto.setType(cart.getType());
            cartDto.setProductId(cart.getProductId());
            cartDto.setId(cart.getId());
            cartDtos.add(cartDto);
        }

        return new RespBean(RespBeanEnum.SUCCESS, cartDtos);
    }


    @PostMapping("/delOrder")
    @AuthCheck
    public RespBean delOrder(String orderId) {
        Order order = orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, orderId));
        if (order.getIsDel() == 1) {
            orderMapper.deleteById(order);
        } else {
            order.setIsDel(1);
            orderMapper.updateById(order);
        }
        return new RespBean(RespBeanEnum.SUCCESS);
    }


    @PostMapping("/restoreOrder")
    @AuthCheck
    public RespBean restoreOrder(String orderId) {
        Order order = orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, orderId));
        order.setIsDel(0);
        orderMapper.updateById(order);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @PostMapping("/payment")
    @AuthCheck
    public RespBean payment(String orderId) {
        Order order = orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, orderId));
        Integer userId = UserContext.getUserId();
        User user = userMapper.selectById(userId);
        BigDecimal money = new BigDecimal(user.getMoney());
        Boolean flag = money.compareTo(order.getTotalPrice()) == -1 ? true : false;

        if (flag) {
            return new RespBean(RespBeanEnum.SUCCESS, "余额不足");
        } else {
            //改order状态
            order.setStatus(1);
            orderMapper.updateById(order);

            //减钱
            money = money.subtract(order.getTotalPrice());
            user.setMoney(money.longValue());
            userMapper.updateById(user);
            //改购物车商品状态
            String cartId = order.getCartId();
            String[] ids = cartId.split(",");
            for (String id : ids) {
                Cart cart = cartMapper.selectById(id);
                cart.setIsPay(1);
                cartMapper.updateById(cart);
                GoodsAttrValue attrValue = goodsAttrValueMapper.selectOne(Wrappers.<GoodsAttrValue>lambdaQuery().eq(GoodsAttrValue::getUnique, cart.getAttrUnique()));
                attrValue.setSale(attrValue.getSale() + cart.getCartNum());
                attrValue.setStock(attrValue.getStock() - cart.getCartNum());
                goodsAttrValueMapper.updateById(attrValue);
                Goods goods = goodsMapper.selectById(cart.getProductId());
                goods.setSale(goods.getSale() + cart.getCartNum());
                goods.setStock(goods.getStock() - cart.getCartNum());
                goodsMapper.updateById(goods);
            }
            return new RespBean(RespBeanEnum.SUCCESS, "支付成功");
        }
    }


    @PostMapping("/confirmOrder")
    @AuthCheck
    public RespBean confirmOrder(@RequestBody List<CartDto> cartDto) {
        Integer uid = UserContext.getUserId();
        Address address = addressMapper.selectOne(Wrappers.<Address>lambdaQuery().eq(Address::getUid, uid).eq(Address::getIsDefault, 1));
        Order order = new Order();
        order.setOrderId(String.valueOf(IdUtil.getSnowflake(1, 20).nextId()));
        order.setAddress(address.getProvince() + address.getCity() + address.getDistrict());
        order.setPhone(address.getPhone());
        order.setIsDel(0);
        order.setUid(uid);
        order.setRealName(address.getRealName());
        order.setUnique(getUUID32());
        String[] ids = new String[cartDto.size()];
        BigDecimal totalPrice = new BigDecimal(0);
        Integer totalNum = 0;
        int index = 0;
        for (CartDto dto : cartDto) {
            totalNum += dto.getCartNum();
            ids[index] = String.valueOf(dto.getId());
            index++;
            totalPrice = dto.getAttrInfo().getPrice().multiply(new BigDecimal(dto.getCartNum())).add(totalPrice);
        }
        User user = userMapper.selectById(uid);
        BigDecimal money = new BigDecimal(user.getMoney());
        Boolean flag = money.compareTo(totalPrice) == -1 ? true : false;
        if (flag) {
            order.setStatus(0);
            for (String id : ids) {
                Cart cart = cartMapper.selectById(id);
                cart.setIsDel(1);
                cartMapper.updateById(cart);
            }
        } else {
            order.setStatus(1);
            money = money.subtract(totalPrice);
            user.setMoney(money.longValue());
            userMapper.updateById(user);
            for (String id : ids) {
                Cart cart = cartMapper.selectById(id);
                cart.setIsPay(1);
                cartMapper.updateById(cart);
            }
            for (CartDto dto : cartDto) {
                GoodsAttrValue attrInfo = dto.getAttrInfo();
                attrInfo.setSale(attrInfo.getSale() + dto.getCartNum());
                attrInfo.setStock(attrInfo.getStock() - dto.getCartNum());
                goodsAttrValueMapper.updateById(attrInfo);
                Goods goods = goodsMapper.selectById(attrInfo.getPid());
                goods.setSale(goods.getSale() + dto.getCartNum());
                goods.setStock(goods.getStock() - dto.getCartNum());
                goodsMapper.updateById(goods);
            }
        }
        order.setTotalNum(totalNum);
        order.setTotalPrice(totalPrice);
        order.setCartId(StringUtils.join(ids, ","));
        orderMapper.insert(order);

        for (CartDto dto : cartDto) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUnique(getUUID32());
            HashMap<Object, Object> map = new HashMap<>();
            map.put("attrInfo", dto.getAttrInfo());
            map.put("storeInfo", dto.getStoreInfo());
            orderInfo.setCartInfo(String.valueOf(JSONObject.toJSON(map)));
            orderInfo.setOid(order.getId());
            orderInfo.setProductId(dto.getProductId());
            orderInfo.setCartId(dto.getId());
            orderInfoMapper.insert(orderInfo);
        }

        if (flag) {
            return new RespBean(RespBeanEnum.SUCCESS, "余额不足");
        } else {
            return new RespBean(RespBeanEnum.SUCCESS, "支付成功");
        }
    }


    @GetMapping("/getOrderList")
    @AuthCheck
    public RespBean getOrderList(Integer status, Integer size, Integer pages) {
        Integer userId = UserContext.getUserId();
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUid, userId).orderByAsc(Order::getStatus).orderByDesc(Order::getUpdateTime);
        if (status != null) {
            if (status == 5) {
                queryWrapper.eq(Order::getIsDel, 1);
            } else {
                queryWrapper.eq(Order::getStatus, status);
                queryWrapper.eq(Order::getIsDel, 0);
            }
        } else {
            queryWrapper.eq(Order::getIsDel, 0);
        }

        Page<Order> page = new Page<>(pages, size);
        Page<Order> orderPage = orderMapper.selectPage(page, queryWrapper);

        List<OrderInfoDto> collect = orderPage.getRecords().stream().map(e -> {
            List<OrderInfo> orderInfos = orderInfoMapper.selectList(Wrappers.lambdaQuery(OrderInfo.class).eq(OrderInfo::getOid, e.getId()));
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            BeanUtils.copyProperties(e, orderInfoDto);
            orderInfoDto.setTotalSize((int) orderPage.getTotal());
            List<GoodsAttrValueDto> info = orderInfos.stream().map(orderInfo -> {
                GoodsAttrValueDto attrValueDto = new GoodsAttrValueDto();
                JSONObject object = JSONObject.parseObject(orderInfo.getCartInfo());
                JSONObject storeInfo = (JSONObject) object.get("storeInfo");
                Goods goods = new Goods();
                goods.setStoreInfo(storeInfo.getString("storeInfo"));
                goods.setStoreName(storeInfo.getString("storeName"));

                JSONObject attrInfo = (JSONObject) object.get("attrInfo");
                GoodsAttrValue attrValue = new GoodsAttrValue();
                attrValue.setPid(Integer.valueOf(attrInfo.getString("pid")));
                attrValue.setImage(attrInfo.getString("image"));
                attrValue.setPrice(new BigDecimal(attrInfo.getString("price")));
                attrValue.setSku(attrInfo.getString("sku"));
                attrValue.setUnique(attrInfo.getString("unique"));
                attrValue.setStock(Integer.valueOf(attrInfo.getString("stock")));
                attrValue.setSale(Integer.valueOf(attrInfo.getString("sale")));
                attrValue.setId(Integer.valueOf(attrInfo.getString("id")));
                BeanUtils.copyProperties(attrValue, attrValueDto);
                LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Cart::getId, orderInfo.getCartId());
                Cart cart = cartMapper.selectOne(wrapper);
                attrValueDto.setCartNum(cart.getCartNum());
                attrValueDto.setStoreInfo(goods);
                return attrValueDto;
            }).collect(Collectors.toList());
            orderInfoDto.setAttrInfo(info);
            return orderInfoDto;
        }).collect(Collectors.toList());

        Page<OrderInfoDto> infoDtoPage = new Page<>();
        BeanUtils.copyProperties(orderPage, infoDtoPage);
        infoDtoPage.setRecords(collect);


        return new RespBean(RespBeanEnum.SUCCESS, infoDtoPage);
    }
}
