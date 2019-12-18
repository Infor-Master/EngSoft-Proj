package edu.ufp.esof.order.services.filters.order;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.FilterI;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class OrderFilterEndDate implements FilterI<OrderItem> {

    private LocalDate endDate;

    public OrderFilterEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public Set<OrderItem> filter(Set<OrderItem> entities) {
        if(this.endDate==null){
            return entities;
        }
        Set<OrderItem> ordersToBeReturned=new HashSet<>();
        for(OrderItem oi: entities){
            if(oi!=null && oi.getOrderDate()!=null && (oi.getOrderDate().isBefore(endDate) || oi.getOrderDate().equals(endDate))){
                ordersToBeReturned.add(oi);
            }
        }
        return ordersToBeReturned;
    }
}
