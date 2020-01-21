package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.QuestaoRespondidaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestaoRespondidaService {

    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private CriterioService criterioService;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public QuestaoRespondidaService(QuestaoRespondidaRepo questaoRespondidaRepo, CriterioService criterioService) {
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.criterioService = criterioService;
    }

    public Set<QuestaoRespondida> findAll(){
        Set<QuestaoRespondida> questaoRespondidas=new HashSet<>();
        for (QuestaoRespondida qr:this.questaoRespondidaRepo.findAll()) {
            questaoRespondidas.add(qr);
        }
        return questaoRespondidas;
    }

    public Optional<QuestaoRespondida> findById(Long id){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=Optional.empty();
        for (QuestaoRespondida qr:this.questaoRespondidaRepo.findAll()) {
            if(qr.getId().compareTo(id)==0){
                optionalQuestaoRespondida=Optional.of(qr);
                break;
            }
        }
        return optionalQuestaoRespondida;
    }

    public Optional<QuestaoRespondida> updateQuestaoRespondida(Long id, QuestaoRespondida questaoRespondida){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            questaoRespondidaRepo.save(questaoRespondida);
            return optionalQuestaoRespondida;
        }
        return Optional.empty();
    }

    public Boolean deleteQuestaoRespondida(Long id){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            delete(optionalQuestaoRespondida.get());
            return true;
        }
        return false;
    }

    public void delete(QuestaoRespondida qr){
        if (qr.getMomento() != null){
            qr.getMomento().getQuestoes().remove(qr);
            qr.setMomento(null);
        }
        if (qr.getEscala() != null){
            criterioService.delete(qr.getEscala());
            qr.setEscala(null);
        }
        questaoRespondidaRepo.delete(qr);
    }

    public void create(Questao q){
        for (Aluno a : q.getMomento().getComponente().getAlunos()) {
            QuestaoRespondida qr = new QuestaoRespondida(q);
            qr.setEscala(new Escala("nota", 0.0f));
            for (MomentoRealizado mr : a.getMomentos()) {
                if (mr.getMomento().getId() == q.getMomento().getId()){
                    mr.getQuestoes().add(qr);
                    questaoRespondidaRepo.save(qr);
                    break;
                }
            }
        }
    }
}
