package edu.ufp.esof.order.services.filters.client;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.FilterI;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ClientFilterStartDate implements FilterI<Client> {
    private LocalDate startDate;

    public ClientFilterStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public Set<Client> filter(Set<Client> entities) {
        if (this.startDate == null) {
            return entities;
        }
        Set<Client> clients = new HashSet<>();
        for (Client client : entities) {
            for (OrderItem oi : client.getOrders()) {
                if (oi != null && oi.getOrderDate() != null && (oi.getOrderDate().isAfter(this.startDate) || oi.getOrderDate().equals(this.startDate))) {
                    clients.add(client);
                }
            }
        }
        return clients;
    }
}
