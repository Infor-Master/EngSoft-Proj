package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.repositories.QuestaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestaoService {

    private QuestaoRepo questaoRepo;

    @Autowired
    public QuestaoService(QuestaoRepo questaoRepo) {
        this.questaoRepo = questaoRepo;
    }

    public Set<Questao> findAll(){
        Set<Questao> questoes=new HashSet<>();
        for(Questao q:this.questaoRepo.findAll()){
            questoes.add(q);
        }
        return questoes;
    }

    public Optional<Questao> findByDesignation(String designation){
        Optional<Questao> optionalQuestao=Optional.empty();
        for (Questao q:this.questaoRepo.findAll()) {
            if(q.getDesignation().compareTo(designation)==0){
                optionalQuestao=Optional.of(q);
                break;
            }
        }
        return optionalQuestao;
    }

    public Optional<Questao> createQuestao(Questao questao){
        Optional<Questao> optionalQuestao=this.questaoRepo.findByDesignation(questao.getDesignation());
        if(optionalQuestao.isPresent()){
            return Optional.empty();
        }
        Questao createdQuestao=this.questaoRepo.save(questao);
        return Optional.of(createdQuestao);
    }


    public Optional<Questao> updateQuestao(String designation, Questao questao){
        Optional<Questao> optionalQuestao=this.questaoRepo.findByDesignation(designation);
        if(optionalQuestao.isPresent()){
            questaoRepo.save(questao);
            return optionalQuestao;
        }
        return Optional.empty();
    }

    public Boolean deleteQuestao(String designation){
        Optional<Questao> optionalQuestao=this.questaoRepo.findByDesignation(designation);
        if(optionalQuestao.isPresent()){
            questaoRepo.delete(optionalQuestao.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        questaoRepo.deleteAll();
    }
}
