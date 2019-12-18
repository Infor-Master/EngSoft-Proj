package edu.ufp.esof.order.controllers;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Client>> getAllClients(){
        this.logger.info("Received a get request");

        return ResponseEntity.ok(this.clientService.findAll());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Client> getClientById(@PathVariable("id") Long id) throws NoClientExcpetion {
        this.logger.info("Received a get request");

        Optional<Client> optionalClient=this.clientService.findById(id);
        if(optionalClient.isPresent()) {
            return ResponseEntity.ok(optionalClient.get());
        }
        throw new NoClientExcpetion(id);
    }

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Client>> searchClients(@RequestParam Map<String,String> query){
        return ResponseEntity.ok(this.clientService.filterClients(query));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> createClient(@RequestBody Client client){
        Optional<Client> clientOptional=this.clientService.createClient(client);
        if(clientOptional.isPresent()){
            return ResponseEntity.ok(clientOptional.get());
        }
        throw new ClientAlreadyExistsExcpetion(client.getName());

    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such client")
    private static class NoClientExcpetion extends RuntimeException {

        public NoClientExcpetion(Long id) {
            super("No such client with id: "+id);
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Client already exists")
    private static class ClientAlreadyExistsExcpetion extends RuntimeException {

        public ClientAlreadyExistsExcpetion(String name) {
            super("A client with name: "+name+" already exists");
        }
    }

}