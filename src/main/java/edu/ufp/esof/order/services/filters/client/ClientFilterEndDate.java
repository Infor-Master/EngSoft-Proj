package edu.ufp.esof.order.services.filters.client;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.filters.FilterI;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ClientFilterEndDate implements FilterI<Client> {

    private LocalDate endDate;

    public ClientFilterEndDate(LocalDate startDate) {
        this.endDate = startDate;
    }

    @Override
    public Set<Client> filter(Set<Client> entities) {
        if (this.endDate == null) {
            return entities;
        }
        Set<Client> clients = new HashSet<>();
        for (Client client : entities) {
            for (OrderItem oi : client.getOrders()) {
                if (oi != null && oi.getOrderDate() != null && (oi.getOrderDate().isBefore(this.endDate) || oi.getOrderDate().equals(this.endDate))) {
                    clients.add(client);
                }
            }
        }
        return clients;
    }
}
