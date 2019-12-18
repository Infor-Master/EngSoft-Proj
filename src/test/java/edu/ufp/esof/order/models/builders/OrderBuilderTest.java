package edu.ufp.esof.order.models.builders;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.OrderItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class OrderBuilderTest {

    @Test
    void build() {

        OrderBuilder orderBuilder=new OrderBuilder();
        orderBuilder.setClient(new Client())
                .setId(10L)
                .setLineOrders(new HashSet<>())
                .setOrderDate(LocalDate.now())
                .setOrderNumber("12345");

        OrderItem orderItem=orderBuilder.build();

        assertNotNull(orderItem.getId());
        assertNotNull(orderItem.getOrderDate());
        assertNotNull(orderItem.getOrderNumber());
        assertNotNull(orderItem.getLineOrders());
        assertNotNull(orderItem.getClient());

    }
}