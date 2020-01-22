package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestaoService {

    private QuestaoRepo questaoRepo;
    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private DocenteRepo docenteRepo;
    private CadeiraRepo cadeiraRepo;
    private MomentoRepo momentoRepo;
    private ResultadoAprendizagemRepo raRepo;
    private QuestaoRespondidaService questaoRespondidaService;

    @Autowired
    public QuestaoService(DocenteRepo docenteRepo, MomentoRepo momentoRepo, ResultadoAprendizagemRepo raRepo, CadeiraRepo cadeiraRepo, QuestaoRepo questaoRepo, QuestaoRespondidaRepo questaoRespondidaRepo, QuestaoRespondidaService questaoRespondidaService) {
        this.questaoRepo = questaoRepo;
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.questaoRespondidaService = questaoRespondidaService;
        this.docenteRepo = docenteRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.raRepo = raRepo;
        this.momentoRepo = momentoRepo;
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
            Set<QuestaoRespondida> auxQrSet = new HashSet<>();
            for (QuestaoRespondida qr : optionalQuestaoRespondidas.get()) {
                auxQrSet.add(qr);
            }
            while (!auxQrSet.isEmpty()){
                Iterator<QuestaoRespondida> questoesRespondidas = auxQrSet.iterator();
                QuestaoRespondida qr = questoesRespondidas.next();
                auxQrSet.remove(qr);
                questaoRespondidaService.delete(qr);

            }
        }
        questaoRepo.delete(q);
    }

    public Optional<Questao> createQuestao(String did, String cadeira, int ano, String componente, String momento, Questao questao, String raNome){
        if (questao.getDesignation() == null){
            return Optional.empty();
        }
        Optional<Componente> optionalComponente = findComponentByType(cadeira,ano,componente);
        Optional<Docente> optionalDocente = docenteRepo.findByCode(did);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()) {
            Componente c = optionalComponente.get();
            ResultadoAprendizagem ra = null;
            for (ResultadoAprendizagem aux : c.getOferta().getRas()) {
                if (aux.getDesignation().equals(raNome)){
                    ra = aux;
                    break;
                }
            }
            if (ra == null){
                return Optional.empty();
            }
            float pesoRA = 0.0f;
            for(Questao aux : ra.getQuestoes()){
                pesoRA += aux.getPesoRA();
            }
            if (pesoRA>=1 || questao.getPesoRA()>(1-pesoRA)){
                return Optional.empty();
            }
            questao.setRa(ra);
            ra.getQuestoes().add(questao);
            if (optionalDocente.get().getComponentes().contains(c)) {
                for (Momento m : c.getMomentos()) {
                    if (m.getDesignation().compareTo(momento) == 0){
                        float pesoM = 0.0f;
                        for (Questao q : m.getQuestoes()) {
                            pesoM += q.getPesoMomento();
                            if (q.getDesignation().equals(questao.getDesignation()) || pesoM >=1){
                                return Optional.empty();
                            }
                        }
                        if (questao.getPesoMomento() > (1-pesoM)){
                            return Optional.empty();
                        }
                        questao.setMomento(m);
                        m.getQuestoes().add(questao);
                        //momentoRepo.save(questao.getMomento());
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
