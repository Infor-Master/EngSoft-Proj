package edu.ufp.esof.order.services.filters;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.order.OrderFilterClientName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderFilterClientNameTest {

    @Test
    void filter() {

        String clientName="client1";

        Set<OrderItem> orders=new HashSet<>();

        Client client=new Client(clientName);

        OrderItem orderItem1=new OrderItem("12345");
        OrderItem orderItem2=new OrderItem("23456");
        OrderItem orderItem3=new OrderItem("34567");
        OrderItem orderItem4=new OrderItem("45678");

        client.addOrder(orderItem1);
        client.addOrder(orderItem3);


        orders.add(orderItem1);
        orders.add(orderItem2);
        orders.add(orderItem3);
        orders.add(orderItem4);

        OrderFilterClientName orderFilterClientName= new OrderFilterClientName(clientName);
        assertEquals(2,orderFilterClientName.filter(orders).size());

        orderFilterClientName= new OrderFilterClientName("non existing client");
        assertEquals(0,orderFilterClientName.filter(orders).size());

        orderFilterClientName= new OrderFilterClientName(null);
        assertEquals(4,orderFilterClientName.filter(orders).size());
    }
}