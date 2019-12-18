package edu.ufp.esof.order.repositories;

import edu.ufp.esof.order.models.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends CrudRepository<OrderItem,Long> {
}
