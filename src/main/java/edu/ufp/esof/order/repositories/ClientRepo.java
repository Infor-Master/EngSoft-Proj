package edu.ufp.esof.order.repositories;

import edu.ufp.esof.order.models.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends CrudRepository<Client,Long> {
    Optional<Client> findByName(String name);


}
