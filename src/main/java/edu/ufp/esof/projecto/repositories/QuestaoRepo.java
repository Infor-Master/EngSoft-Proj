package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Questao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoRepo extends CrudRepository<Questao,Long> {
}
