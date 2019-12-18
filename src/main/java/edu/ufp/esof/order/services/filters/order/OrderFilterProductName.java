package edu.ufp.esof.order.services.filters.order;

import edu.ufp.esof.order.models.LineOrder;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class OrderFilterProductName implements FilterI<OrderItem> {

    private String productName;

    public OrderFilterProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public Set<OrderItem> filter(Set<OrderItem> entities) {
        if(this.productName==null || this.productName.isBlank()){
            return entities;
        }

        Set<OrderItem> ordersToBeReturned=new HashSet<>();
        for(OrderItem oi: entities){
            for(LineOrder lo:oi.getLineOrders()){
                if(lo!=null && lo.getProduct()!=null && lo.getProduct().getName().equals(productName)){
                    ordersToBeReturned.add(oi);
                    break;
                }
            }
        }

        return ordersToBeReturned;
    }
}
