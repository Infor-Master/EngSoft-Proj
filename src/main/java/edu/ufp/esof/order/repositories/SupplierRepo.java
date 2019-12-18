package edu.ufp.esof.order.repositories;

import edu.ufp.esof.order.models.Supplier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepo extends CrudRepository<Supplier,Long> {
}
