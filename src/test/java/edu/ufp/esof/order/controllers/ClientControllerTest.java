package edu.ufp.esof.order.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClient() throws Exception {
        Client client=new Client("client1");

        String jsonRequest=this.objectMapper.writeValueAsString(client);

        when(clientService.createClient(client)).thenReturn(Optional.of(client));

        this.mockMvc.perform(
                post("/client").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(
                status().isOk()
        );


        Client existingClient=new Client("client2");

        when(this.clientService.createClient(existingClient)).thenReturn(Optional.empty());

        String existingClientJson=this.objectMapper.writeValueAsString(existingClient);

        this.mockMvc.perform(
                post("/client").contentType(MediaType.APPLICATION_JSON).content(existingClientJson)
        ).andExpect(
                status().isBadRequest()
        );

    }

    @Test
    void getAllClients() {
    }

    @Test
    void getClientById() throws Exception {
        Client client=new Client("client1");
        client.setId(1L);

        when(this.clientService.findById(1L)).thenReturn(Optional.of(client));

        String responseJson=this.mockMvc.perform(
                get("/client/1")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        //System.out.println(responseJson);

        Client responseClient=this.objectMapper.readValue(responseJson,Client.class);
        assertEquals(client,responseClient);

        this.mockMvc.perform(
                get("/client/2")
        ).andExpect(
                status().isNotFound()
        );

        //System.out.println(responseClient);

    }

}