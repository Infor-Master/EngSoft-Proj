package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Cadeira;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadeiraRepo extends CrudRepository<Cadeira, Long> {
    Optional<Cadeira> findByDesignation(String name);
    Optional<Cadeira> findByCode(String code);
}
