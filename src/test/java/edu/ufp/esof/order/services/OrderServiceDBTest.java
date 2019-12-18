package edu.ufp.esof.order.services;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.repositories.OrderRepo;
import edu.ufp.esof.order.services.authentication.LoginService;
import edu.ufp.esof.order.services.filters.order.FilterOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderServiceDB.class)
class OrderServiceDBTest {

    @MockBean
    private LoginService loginService;

    @Autowired
    private OrderServiceDB orderServiceDB;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private FilterOrderService filterOrderService;

    @MockBean
    private ClientService clientService;

    @Test
    void accessOrder() {
        Client client1=new Client();
        client1.setName("client1");

        OrderItem orderItem=new OrderItem();
        client1.addOrder(orderItem);

        String token="token";

        when(loginService.authenticateUser(client1,token)).thenReturn(true);
        when(this.orderRepo.findById(1L)).thenReturn(Optional.of(orderItem));

        assertNotNull(orderServiceDB.accessOrder(1L,token));
    }
}