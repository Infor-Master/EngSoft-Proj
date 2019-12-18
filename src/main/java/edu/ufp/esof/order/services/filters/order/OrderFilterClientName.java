package edu.ufp.esof.order.services.filters.order;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class OrderFilterClientName implements FilterI<OrderItem> {

    private String clientName;

    public OrderFilterClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public Set<OrderItem> filter(Set<OrderItem> entities) {
        if(this.clientName==null || this.clientName.isBlank()){
            return entities;
        }

        Set<OrderItem> ordersToBeReturned=new HashSet<>();
        for(OrderItem oi: entities){
            if(oi.getClient()!=null && oi.getClient().getName()!=null
                    && oi.getClient().getName().equals(clientName)){
                ordersToBeReturned.add(oi);
            }
        }
        return ordersToBeReturned;
    }
}
