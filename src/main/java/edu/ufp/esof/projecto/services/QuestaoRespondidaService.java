package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.QuestaoRespondida;
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

    public Optional<QuestaoRespondida> createQuestaoRespondida(QuestaoRespondida questaoRespondida){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(questaoRespondida.getId());
        if(optionalQuestaoRespondida.isPresent()){
            return Optional.empty();
        }
        QuestaoRespondida createdQuestaoRespondida=this.questaoRespondidaRepo.save(questaoRespondida);
        return Optional.of(createdQuestaoRespondida);
    }

    /**
     *  Boolean não usado ainda mas poderá ser necessário
     */
    public Boolean deleteQuestaoRespondida(Long id){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            delete(optionalQuestaoRespondida.get());
            //questaoRespondidaRepo.delete(optionalQuestaoRespondida.get());
            return true;
        }
        return false;
    }

    public void delete(QuestaoRespondida qr){
        if (qr.getMomento() != null){
            qr.getMomento().getQuestoes().remove(qr);
            qr.setMomento(null);
        }
        if (qr.getCriterio() != null){
            criterioService.delete(qr.getCriterio());
            qr.setCriterio(null);
        }
        questaoRespondidaRepo.delete(qr);
    }
}
