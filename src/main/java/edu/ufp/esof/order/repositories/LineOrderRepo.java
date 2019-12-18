package edu.ufp.esof.order.repositories;

import edu.ufp.esof.order.models.LineOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineOrderRepo extends CrudRepository<LineOrder,Long> {
}
