package edu.ufp.esof.order.services.filters.client;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.AndFilter;
import edu.ufp.esof.order.services.filters.FilterI;
import edu.ufp.esof.order.services.filters.order.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterClientService {

    public Set<Client> filter(Set<Client> clients, FilterClientObject filterClientObject) {
        FilterI<Client> clientFilterByName=new ClientFilterByName(filterClientObject.getName());
        FilterI<Client> clientFilterStartDate=new ClientFilterStartDate(filterClientObject.getStartDate());

        FilterI<Client> clientFilterEndDate=new ClientFilterEndDate(filterClientObject.getEndDate());


        FilterI<Client> productNameAndClientName=new AndFilter<>(clientFilterByName,clientFilterStartDate);

        return new AndFilter<>(productNameAndClientName,clientFilterEndDate).filter(clients);
    }


}
