package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Criterio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CriterioRepo extends CrudRepository<Criterio,Long> {
    Optional<Criterio> findByDesignation(String designation);
}
