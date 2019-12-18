package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.MomentoRealizado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentoRealizadoRepo extends CrudRepository<MomentoRealizado,Long> {
}
