package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Momento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentoRepo extends CrudRepository<Momento, Long> {
}
