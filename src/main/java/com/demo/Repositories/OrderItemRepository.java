package com.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.Entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
