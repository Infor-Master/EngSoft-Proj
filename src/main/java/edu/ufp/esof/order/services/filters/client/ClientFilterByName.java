package edu.ufp.esof.order.services.filters.client;


import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class ClientFilterByName implements FilterI<Client> {

    private String name;

    public ClientFilterByName(String name) {
        this.name = name;
    }

    @Override
    public Set<Client> filter(Set<Client> entities) {
        if(name==null || name.isEmpty()){
            return entities;
        }
        Set<Client> clients=new HashSet<>();
        for(Client client:entities){
            if(client.getName()!=null && client.getName().equalsIgnoreCase(name)){
                clients.add(client);
            }
        }
        return clients;
    }
}
