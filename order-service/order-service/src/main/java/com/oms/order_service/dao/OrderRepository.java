package com.oms.order_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oms.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
