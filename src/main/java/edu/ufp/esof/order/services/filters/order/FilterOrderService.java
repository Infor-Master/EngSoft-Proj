package edu.ufp.esof.order.services.filters.order;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.AndFilter;
import edu.ufp.esof.order.services.filters.FilterI;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterOrderService{

    public Set<OrderItem> filter(Set<OrderItem> orders, FilterOrderObject filterOrderObject) {
        FilterI<OrderItem> productNameFIlter=new OrderFilterProductName(filterOrderObject.getProductName());
        FilterI<OrderItem> clientNameFilter=new OrderFilterClientName(filterOrderObject.getClientName());

        FilterI<OrderItem> startDateFilter=new OrderFilterStartDate(filterOrderObject.getStartDate());
        FilterI<OrderItem> endDateFilter=new OrderFilterEndDate(filterOrderObject.getEndDate());

        FilterI<OrderItem> productNameAndClientName=new AndFilter<>(productNameFIlter,clientNameFilter);
        FilterI<OrderItem> startDateAndEndDate=new AndFilter<>(startDateFilter,endDateFilter);

        return new AndFilter<>(productNameAndClientName,startDateAndEndDate).filter(orders);
    }
}
