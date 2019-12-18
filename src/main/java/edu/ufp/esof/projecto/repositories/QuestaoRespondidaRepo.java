package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.QuestaoRespondida;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoRespondidaRepo extends CrudRepository<QuestaoRespondida,Long> {
}
