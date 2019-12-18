package edu.ufp.esof.order.models.builders;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.LineOrder;
import edu.ufp.esof.order.models.OrderItem;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class OrderBuilder {
    private Long id;
    private String orderNumber;
    private LocalDate orderDate;
    private Client client;
    private Set<LineOrder> lineOrders=new HashSet<>();

    public OrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public OrderBuilder setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public OrderBuilder setClient(Client client) {
        this.client = client;
        return this;
    }

    public OrderBuilder setLineOrders(Set<LineOrder> lineOrders) {
        this.lineOrders = lineOrders;
        return this;
    }

    public OrderBuilder addLineOrder(LineOrder lineOrder){
        this.lineOrders.add(lineOrder);
        return this;
    }

    public OrderItem build(){
        return new OrderItem(id,orderNumber,orderDate,client,lineOrders);
    }
}
