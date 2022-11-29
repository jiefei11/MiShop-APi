package com.bhgeek.mishopapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.UserContext;
import com.bhgeek.mishopapi.dto.CartDto;
import com.bhgeek.mishopapi.entity.Cart;
import com.bhgeek.mishopapi.entity.Goods;
import com.bhgeek.mishopapi.entity.GoodsAttrValue;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.mapper.CartMapper;
import com.bhgeek.mishopapi.mapper.GoodsAttrValueMapper;
import com.bhgeek.mishopapi.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-25
 */
@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private GoodsAttrValueMapper goodsAttrValueMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @PostMapping("/addCart")
    @AuthCheck
    public RespBean addCart(String unique, Integer cartNum, Integer pid) {
        Cart one = cartMapper.selectOne(new LambdaQueryWrapper<Cart>().eq(Cart::getAttrUnique, unique).eq(Cart::getIsDel, 0).eq(Cart::getIsPay, 0).eq(Cart::getUid, UserContext.getUserId()));
        if (one == null) {
            Cart cart = new Cart();
            Integer userId = UserContext.getUserId();
            cart.setCartNum(cartNum);
            cart.setType("product");
            cart.setUid(userId);
            cart.setIsDel(0);
            cart.setIsPay(0);
            cart.setAttrUnique(unique);
            cart.setProductId(pid);
            cartMapper.insert(cart);
        } else {
            one.setCartNum(one.getCartNum() + cartNum);
            cartMapper.updateById(one);
        }
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @GetMapping
    @AuthCheck
    public RespBean getCart() {
        Integer userId = UserContext.getUserId();
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUid, userId).eq(Cart::getIsDel, 0).eq(Cart::getIsPay, 0);
        List<Cart> carts = cartMapper.selectList(queryWrapper);
        List<CartDto> collect = carts.stream().map(e -> {
            CartDto cartDto = new CartDto();
            GoodsAttrValue attrValue = goodsAttrValueMapper.selectOne(new LambdaQueryWrapper<GoodsAttrValue>().eq(GoodsAttrValue::getUnique, e.getAttrUnique()));
            Goods goods = goodsMapper.selectOne(new LambdaQueryWrapper<Goods>().eq(Goods::getId, e.getProductId()));
            cartDto.setStoreInfo(goods);
            cartDto.setAttrInfo(attrValue);
            cartDto.setCartNum(e.getCartNum());
            cartDto.setType(e.getType());
            cartDto.setProductId(e.getProductId());
            cartDto.setId(e.getId());
            return cartDto;
        }).collect(Collectors.toList());
        return new RespBean(RespBeanEnum.SUCCESS, collect);
    }

    @PostMapping("/delCart")
    @AuthCheck
    public RespBean delCart(Integer id) {
        Cart cart = cartMapper.selectById(id);
        cart.setIsDel(1);
        cartMapper.updateById(cart);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @PostMapping("/uptNum")
    @AuthCheck
    public RespBean uptNum(Integer id, Integer cartNum) {
        Cart cart = cartMapper.selectById(id);
        cart.setCartNum(cartNum);
        cartMapper.updateById(cart);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

}
