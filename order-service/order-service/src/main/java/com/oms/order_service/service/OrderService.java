package com.oms.order_service.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.order_service.dao.OrderRepository;
import com.oms.order_service.exception.ResourceNotFoundException;
import com.oms.order_service.model.Order;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repo;
	
    public List<Order> getAllOrders() {
        return repo.findAll();
    }
    public Order getOrderById(Long id) {
    	return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }
    public Order saveOrder(Order order) {
    	return repo.save(order);
    }

    public void deleteOrder(Long id) {
        repo.deleteById(id);
    }
}
