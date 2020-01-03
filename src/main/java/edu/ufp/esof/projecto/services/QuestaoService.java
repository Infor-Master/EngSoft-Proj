package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.QuestaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestaoService {

    private QuestaoRepo questaoRepo;

    @Autowired
    public QuestaoService(QuestaoRepo questaoRepo) {
        this.questaoRepo = questaoRepo;
    }
}
