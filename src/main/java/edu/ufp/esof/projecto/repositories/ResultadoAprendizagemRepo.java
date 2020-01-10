package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.ResultadoAprendizagem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultadoAprendizagemRepo extends CrudRepository<ResultadoAprendizagem,Long> {
    Optional<ResultadoAprendizagem> findByDesignation(String designation);
}
