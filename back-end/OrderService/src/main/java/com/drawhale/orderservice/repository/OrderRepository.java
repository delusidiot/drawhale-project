package com.drawhale.orderservice.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderId(String orderId);
    Iterable<OrderEntity> findByUserId(String userId);
}
