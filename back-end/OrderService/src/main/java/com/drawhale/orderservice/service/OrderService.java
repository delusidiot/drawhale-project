package com.drawhale.orderservice.service;

import com.drawhale.orderservice.dto.OrderDto;
import com.drawhale.orderservice.repository.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrderByUserId(String userId);
}
