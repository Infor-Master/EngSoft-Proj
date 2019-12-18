package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.ResultadoAprendizagem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadoAprendizagemRepo extends CrudRepository<ResultadoAprendizagem,Long> {
}
