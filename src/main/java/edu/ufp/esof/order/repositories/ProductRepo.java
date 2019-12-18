package edu.ufp.esof.order.repositories;

import edu.ufp.esof.order.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends CrudRepository<Product,Long> {
}
