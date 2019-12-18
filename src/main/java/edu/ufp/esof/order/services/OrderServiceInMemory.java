package edu.ufp.esof.order.services;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.authentication.LoginService;
import edu.ufp.esof.order.services.filters.order.FilterOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Profile(value = "inmemory")
public class OrderServiceInMemory extends OrderServiceAbstraction{

    private Set<OrderItem> orders=new HashSet<>();
    private static Long id_count;

    @Autowired
    public OrderServiceInMemory(FilterOrderService filterOrderService, LoginService loginService,ClientService clientService) {
        super(filterOrderService, loginService,clientService);
    }

    @Override
    public Optional<OrderItem> createOrder(OrderItem order) {
        return Optional.empty();
    }

    @Override
    public Set<OrderItem> findAll() {
        Iterable<OrderItem> orderItems= this.orders;

        Set<OrderItem> orderSet=new HashSet<>();
        for(OrderItem oi:orderItems){
            orderSet.add(oi);
        }

        return orderSet;
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        for(OrderItem order:this.orders){
            if(order.getId().equals(id)){
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

}
