package edu.ufp.esof.order.services.orderoutput;

import edu.ufp.esof.order.models.OrderItem;

public interface OrderOutput {
    byte[] outputFile(OrderItem order,String type);
}
