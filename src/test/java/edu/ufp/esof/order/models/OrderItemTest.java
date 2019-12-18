package edu.ufp.esof.order.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class OrderItemTest {

    @Test
    void addLineOrder() {

    }

    @Test
    void price() {
        OrderItem orderItem=new OrderItem();

        LineOrder lineOrder=new LineOrder();
        lineOrder.setProduct(new Product("prod1",10));
        lineOrder.setQuantity(2);

        LineOrder lineOrder2=new LineOrder();
        lineOrder2.setProduct(new Product("prod2",20));
        lineOrder2.setQuantity(1);

        orderItem.addLineOrder(lineOrder);
        orderItem.addLineOrder(lineOrder2);

        assertEquals(40,orderItem.price(),0.1);
        assertEquals(2,orderItem.getLineOrders().size());
    }
}