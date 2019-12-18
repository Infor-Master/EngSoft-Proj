package edu.ufp.esof.order.services;

import edu.ufp.esof.order.models.OrderItem;

import java.util.Optional;
import java.util.Set;

public interface OrderService {


    Set<OrderItem> findAll();

    Optional<OrderItem> findById(Long id);
    Optional<OrderItem> createOrder(OrderItem order);
}
