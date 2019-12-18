package edu.ufp.esof.order.services.filters;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.order.OrderFilterStartDate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderFilterOrderStartDateTest {

    @Test
    void filter() {

        Set<OrderItem> orders=new HashSet<>();

        OrderItem orderItem1=new OrderItem("12345");
        OrderItem orderItem2=new OrderItem("23456");
        OrderItem orderItem3=new OrderItem("34567");
        OrderItem orderItem4=new OrderItem("45678");

        orderItem1.setOrderDate(LocalDate.now().minusDays(3));

        orders.add(orderItem1);
        orders.add(orderItem2);
        orders.add(orderItem3);
        orders.add(orderItem4);

        OrderFilterStartDate orderFilterOrderEndDate=new OrderFilterStartDate(LocalDate.now().minusDays(1));
        assertEquals(3,orderFilterOrderEndDate.filter(orders).size());

        orderFilterOrderEndDate= new OrderFilterStartDate(LocalDate.now().plusDays(1));
        assertEquals(0,orderFilterOrderEndDate.filter(orders).size());

        orderFilterOrderEndDate= new OrderFilterStartDate(null);
        assertEquals(4,orderFilterOrderEndDate.filter(orders).size());

    }
}