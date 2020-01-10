package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Momento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MomentoRepo extends CrudRepository<Momento, Long> {
    Optional<Momento> findByDesignation(String designation);
}
