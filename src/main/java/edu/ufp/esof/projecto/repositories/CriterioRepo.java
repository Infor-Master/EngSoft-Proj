package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Escala;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CriterioRepo extends CrudRepository<Escala,Long> {
    Optional<Escala> findByDesignation(String designation);
}
