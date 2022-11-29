package com.bhgeek.mishopapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bhgeek.mishopapi.entity.Order;
import com.bhgeek.mishopapi.mapper.OrderMapper;
import com.bhgeek.mishopapi.service.IOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
}
