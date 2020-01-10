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
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public QuestaoRespondidaService(QuestaoRespondidaRepo questaoRespondidaRepo) {
        this.questaoRespondidaRepo = questaoRespondidaRepo;
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

    public Boolean deleteQuestaoRespondida(Long id){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            questaoRespondidaRepo.delete(optionalQuestaoRespondida.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        questaoRespondidaRepo.deleteAll();
    }
}
