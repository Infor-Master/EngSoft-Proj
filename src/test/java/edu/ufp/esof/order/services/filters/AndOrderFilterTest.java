package edu.ufp.esof.order.services.filters;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.LineOrder;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.models.Product;
import edu.ufp.esof.order.services.filters.order.OrderFilterClientName;
import edu.ufp.esof.order.services.filters.order.OrderFilterProductName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AndOrderFilterTest {

    @Test
    void filter() {

        String productName="product1";
        String clientName="client1";

        Client client=new Client(clientName);

        Set<OrderItem> orders=new HashSet<>();

        Product product=new Product(productName);

        OrderItem orderItem1=new OrderItem("12345");
        OrderItem orderItem2=new OrderItem("23456");
        OrderItem orderItem3=new OrderItem("34567");
        OrderItem orderItem4=new OrderItem("45678");

        LineOrder lineOrder1=new LineOrder(product,1);

        orderItem1.addLineOrder(lineOrder1);

        LineOrder lineOrder2=new LineOrder(new Product("product2"),1);

        orderItem2.addLineOrder(lineOrder1);
        orderItem2.addLineOrder(lineOrder2);

        client.addOrder(orderItem1);
        client.addOrder(orderItem3);

        orders.add(orderItem1);
        orders.add(orderItem2);
        orders.add(orderItem3);
        orders.add(orderItem4);

        OrderFilterProductName orderFilterProductName= new OrderFilterProductName(productName);
        OrderFilterClientName orderFilterClientName= new OrderFilterClientName(clientName);
        AndFilter andOrderFilter =new AndFilter(orderFilterProductName,orderFilterClientName);
        assertEquals(1, andOrderFilter.filter(orders).size());

        orderFilterClientName= new OrderFilterClientName("non existing client");
        andOrderFilter =new AndFilter(orderFilterProductName,orderFilterClientName);
        assertEquals(0, andOrderFilter.filter(orders).size());


    }
}