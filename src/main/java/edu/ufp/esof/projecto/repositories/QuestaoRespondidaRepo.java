package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.QuestaoRespondida;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestaoRespondidaRepo extends CrudRepository<QuestaoRespondida,Long> {
    Optional<Iterable<QuestaoRespondida>> findAllByQuestao(Questao questao);
}
