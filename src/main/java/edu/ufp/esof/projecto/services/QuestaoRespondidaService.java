package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.QuestaoRespondidaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestaoRespondidaService {

    private QuestaoRespondidaRepo questaoRespondidaRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public QuestaoRespondidaService(QuestaoRespondidaRepo questaoRespondidaRepo) {
        this.questaoRespondidaRepo = questaoRespondidaRepo;
    }
}
