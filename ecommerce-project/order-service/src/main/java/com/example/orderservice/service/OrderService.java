package com.example.orderservice.service;


import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.vo.ResponseOrder;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);

    OrderDto getOrderByOrderId(String orderId);

    List<ResponseOrder> getAllOrdersByUserId(String userId);
}
