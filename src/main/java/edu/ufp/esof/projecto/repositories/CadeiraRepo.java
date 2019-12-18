package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Cadeira;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadeiraRepo extends CrudRepository<Cadeira, Long> {
}
