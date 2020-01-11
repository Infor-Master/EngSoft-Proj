package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.models.Questao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MomentoRealizadoRepo extends CrudRepository<MomentoRealizado,Long> {
    Optional<Iterable<MomentoRealizado>> findAllByMomento (Momento momento);
}
