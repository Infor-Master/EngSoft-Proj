package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Docente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepo extends CrudRepository<Docente, Long> {
    Optional<Docente> findByCode(String code);
}
