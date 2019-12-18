package edu.ufp.esof.order.services;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.services.authentication.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LoginService.class)
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @MockBean
    private ClientService clientService;

    @Test
    void authenticate() {

        Client client=new Client("username");
        client.setPassword("password");

        when(clientService.findByName("username")).thenReturn(Optional.of(client));

        loginService.addUser("username","password");

        assertTrue(loginService.generateToken("username","password").isPresent());
        assertFalse(loginService.generateToken("username","password1").isPresent());

    }
}