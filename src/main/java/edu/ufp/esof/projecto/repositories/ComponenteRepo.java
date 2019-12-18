package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Componente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteRepo extends CrudRepository<Componente, Long> {
}
