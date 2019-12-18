package edu.ufp.esof.order.services.filters.order;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.FilterI;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class OrderFilterStartDate implements FilterI<OrderItem> {

    private LocalDate startDate;

    public OrderFilterStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public Set<OrderItem> filter(Set<OrderItem> entities) {
        if(this.startDate==null){
            return entities;
        }
        Set<OrderItem> ordersToReturn=new HashSet<>();
        for(OrderItem oi: entities){
            if(oi!=null && oi.getOrderDate()!=null && (oi.getOrderDate().isAfter(this.startDate) || oi.getOrderDate().equals(this.startDate))){
                ordersToReturn.add(oi);
            }
        }
        return ordersToReturn;
    }
}
