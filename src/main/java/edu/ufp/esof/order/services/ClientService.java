package edu.ufp.esof.order.services;


import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.repositories.ClientRepo;
import edu.ufp.esof.order.services.filters.client.FilterClientObject;
import edu.ufp.esof.order.services.filters.client.FilterClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Service
public class ClientService {

    private ClientRepo clientRepo;
    private FilterClientService filterService;

    @Autowired
    public ClientService(ClientRepo clientRepo, FilterClientService filterService) {
        this.clientRepo=clientRepo;
        this.filterService = filterService;
    }

    public Set<Client> findAll() {
        Set<Client> clients=new HashSet<>();
        for(Client client:this.clientRepo.findAll()){
            clients.add(client);
        }
        return clients;
    }

    public Optional<Client> findById(Long id) {
        return this.clientRepo.findById(id);
    }

    public Optional<Client> createClient(Client client) {
        Optional<Client> optionalClient=this.clientRepo.findByName(client.getName());
        if(optionalClient.isPresent()){
          return Optional.empty();
        }
        Client createdClient=this.clientRepo.save(client);
        return Optional.of(createdClient);
    }

    public Set<Client> filterClients(Map<String, String> searchParams) {

        FilterClientObject filterClientObject =new FilterClientObject(searchParams);
        Set<Client> clients=this.findAll();

        return this.filterService.filter(clients, filterClientObject);
    }

    public Optional<Client> findByName(String name) {
        return this.clientRepo.findByName(name);
    }
}
