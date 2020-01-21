package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.DocenteRepo;
import edu.ufp.esof.projecto.repositories.QuestaoRespondidaRepo;
import edu.ufp.esof.projecto.repositories.QuestaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestaoService {

    private QuestaoRepo questaoRepo;
    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private DocenteRepo docenteRepo;
    private CadeiraRepo cadeiraRepo;
    private QuestaoRespondidaService questaoRespondidaService;

    @Autowired
    public QuestaoService(DocenteRepo docenteRepo, CadeiraRepo cadeiraRepo, QuestaoRepo questaoRepo, QuestaoRespondidaRepo questaoRespondidaRepo, QuestaoRespondidaService questaoRespondidaService) {
        this.questaoRepo = questaoRepo;
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.questaoRespondidaService = questaoRespondidaService;
        this.docenteRepo = docenteRepo;
        this.cadeiraRepo = cadeiraRepo;
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

    public Set<QuestaoRespondida> findAllByQuestao(Questao questao){
        Set<QuestaoRespondida> questoesRespondidas=new HashSet<>();
        for (QuestaoRespondida qr:this.questaoRespondidaRepo.findAll()) {
            if(qr.getQuestao().getId().compareTo(questao.getId())==0){
                questoesRespondidas.add(qr);
            }
        }
        return questoesRespondidas;
    }

    // falta fazer
    public Boolean deleteQuestao(String did, String cadeira, int ano, String comp, String momento, String designation){
        Optional<Componente> optionalComponente = findComponentByType(cadeira,ano,comp);
        Optional<Docente> optionalDocente = docenteRepo.findByCode(did);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()) {
            if (optionalDocente.get().getComponentes().contains(optionalComponente.get())) {
                for (Momento m : optionalComponente.get().getMomentos()) {
                    if (m.getDesignation().compareTo(momento) == 0){
                        for (Questao q : m.getQuestoes()) {
                            if (q.getDesignation().compareTo(designation) == 0){
                                delete(q);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void delete(Questao q){
        if (q.getMomento() != null){
            q.getMomento().getQuestoes().remove(q);
            q.setMomento(null);
        }
        if (q.getRa() != null){
            q.getRa().getQuestoes().remove(q);
            q.setRa(null);
        }
        Optional<Iterable<QuestaoRespondida>> optionalQuestaoRespondidas = questaoRespondidaRepo.findAllByQuestao(q);
        if (optionalQuestaoRespondidas.isPresent()){
            while(!optionalQuestaoRespondidas.isEmpty()){
                Iterator<QuestaoRespondida> questoesRespondidas = optionalQuestaoRespondidas.get().iterator();
                QuestaoRespondida qr = questoesRespondidas.next();
                questaoRespondidaService.delete(qr);
            }
        }
        questaoRepo.delete(q);
    }

    public Optional<Questao> createQuestao(String did, String cadeira, int ano, String componente, String momento, Questao questao){
        if (questao.getDesignation() == null){
            return Optional.empty();
        }
        Optional<Componente> optionalComponente = findComponentByType(cadeira,ano,componente);
        Optional<Docente> optionalDocente = docenteRepo.findByCode(did);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()) {
            if (optionalDocente.get().getComponentes().contains(optionalComponente.get())) {
                for (Momento m : optionalComponente.get().getMomentos()) {
                    if (m.getDesignation().compareTo(momento) == 0){
                        float pesoRA = 0.0f;
                        float pesoM = 0.0f;
                        for (Questao q : m.getQuestoes()) {
                            pesoRA += q.getPesoRA();
                            pesoM += q.getPesoMomento();
                            if (q.getDesignation().compareTo(questao.getDesignation()) == 0 || pesoM >1 || pesoRA>1){
                                return Optional.empty();
                            }
                        }
                        questao.setMomento(m);
                        m.getQuestoes().add(questao);
                        questaoRepo.save(questao);
                        questaoRespondidaService.create(questao);
                        return Optional.of(questao);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Componente> findComponentByType(String cadeira, int ano, String type) {
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        Optional<Componente> optionalComponente = Optional.empty();
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno() == ano){
                    for (Componente c : o.getComponentes()) {
                        if (c.getType().compareTo(type) == 0){
                            optionalComponente = Optional.of(c);
                            return optionalComponente;
                        }
                    }
                }
            }
        }
        return optionalComponente;
    }
}
