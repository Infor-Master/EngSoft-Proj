package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.DocenteRepo;
import edu.ufp.esof.projecto.repositories.MomentoRealizadoRepo;
import edu.ufp.esof.projecto.repositories.MomentoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class MomentoService {

    private MomentoRepo momentoRepo;
    private DocenteRepo docenteRepo;
    private CadeiraRepo cadeiraRepo;
    private MomentoRealizadoRepo momentoRealizadoRepo;
    private QuestaoService questaoService;
    private MomentoRealizadoService momentoRealizadoService;


    @Autowired
    public MomentoService(DocenteRepo docenteRepo, CadeiraRepo cadeiraRepo, MomentoRepo momentoRepo, MomentoRealizadoRepo momentoRealizadoRepo, QuestaoService questaoService, MomentoRealizadoService momentoRealizadoService) {
        this.momentoRepo = momentoRepo;
        this.momentoRealizadoRepo = momentoRealizadoRepo;
        this.questaoService = questaoService;
        this.momentoRealizadoService = momentoRealizadoService;
        this.docenteRepo = docenteRepo;
        this.cadeiraRepo = cadeiraRepo;
    }


    public Set<Momento> findAll(){
        Set<Momento> Momentos=new HashSet<>();
        for(Momento m:this.momentoRepo.findAll()){
            Momentos.add(m);
        }
        return Momentos;
    }

    public Optional<Momento> findByDesignation(String designation){
        Optional<Momento> optionalMomento = Optional.empty();
        for(Momento m:this.momentoRepo.findAll()){
            if (m.getDesignation().compareTo(designation) == 0){
                optionalMomento = Optional.of(m);
                break;
            }
        }
        return optionalMomento;
    }

    public Set<MomentoRealizado> findAllByMomento(Momento momento){
        Set<MomentoRealizado> momentosRealizados=new HashSet<>();
        for (MomentoRealizado mr:this.momentoRealizadoRepo.findAll()) {
            if(mr.getMomento().getId().compareTo(momento.getId())==0){
                momentosRealizados.add(mr);
            }
        }
        return momentosRealizados;
    }



    public Boolean deleteMomento(String cadeira, int ano, String comp, String designation){
        Optional<Componente> optionalComponente = findComponenteByType(cadeira,ano,comp);
        if (optionalComponente.isPresent()){
            for (Momento m:optionalComponente.get().getMomentos()) {
                if (m.getDesignation().compareTo(designation) == 0){
                    delete(m);
                    return true;
                }
            }
        }
        return false;
    }

    public void delete(Momento m){
        if (m.getComponente() != null){
            m.getComponente().getMomentos().remove(m);
            m.setComponente(null);
        }
        while(!m.getQuestoes().isEmpty()){
            Iterator<Questao> questoes = m.getQuestoes().iterator();
            Questao q = questoes.next();
            questaoService.delete(q);
        }
        /*
        for (Questao q : m.getQuestoes()) {
            questaoService.delete(q);
        }*/
        Optional<Iterable<MomentoRealizado>> optionalMomentoRealizado =momentoRealizadoRepo.findAllByMomento(m);
        if (optionalMomentoRealizado.isPresent()){
            for (MomentoRealizado mr : optionalMomentoRealizado.get()) {
                momentoRealizadoService.delete(mr);
            }
        }
        momentoRepo.delete(m);
    }

    public Optional<Momento> createMomento(String id, String cadeira, int ano, String comp, Momento momento){
        if (momento.getDesignation() == null){
            return Optional.empty();
        }
        Optional<Componente> optionalComponente = findComponenteByType(cadeira,ano,comp);
        Optional<Docente> optionalDocente = docenteRepo.findByCode(id);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()){
            if (optionalDocente.get().getComponentes().contains(optionalComponente.get())){
                Float peso = 0.0f;
                for (Momento m : optionalComponente.get().getMomentos()) {
                    peso = peso + m.getPeso();
                    if (m.getDesignation().compareTo(momento.getDesignation()) == 0 || peso>1.0f){
                        return Optional.empty();
                    }
                }
                momento.setComponente(optionalComponente.get());
                optionalComponente.get().getMomentos().add(momento);
                momentoRepo.save(momento);
                //componenteRepo.save(optionalComponente.get()); // verificar se deve estar ou nao
                momentoRealizadoService.create(momento);
                return Optional.of(momento);
            }
        }
        return Optional.empty();
    }


    public Optional<Componente> findComponenteByType(String cadeira, int ano, String type) {
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

