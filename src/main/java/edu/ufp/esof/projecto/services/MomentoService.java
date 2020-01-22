package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.models.builders.MomentoRealizadoBuilder;
import edu.ufp.esof.projecto.models.builders.QuestaoRespondidaBuilder;
import edu.ufp.esof.projecto.repositories.*;
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
    private ComponenteRepo componenteRepo;
    private MomentoRealizadoRepo momentoRealizadoRepo;
    private QuestaoService questaoService;
    private MomentoRealizadoService momentoRealizadoService;


    @Autowired
    public MomentoService(DocenteRepo docenteRepo, CadeiraRepo cadeiraRepo, ComponenteRepo componenteRepo, MomentoRepo momentoRepo, MomentoRealizadoRepo momentoRealizadoRepo, QuestaoService questaoService, MomentoRealizadoService momentoRealizadoService) {
        this.momentoRepo = momentoRepo;
        this.momentoRealizadoRepo = momentoRealizadoRepo;
        this.questaoService = questaoService;
        this.momentoRealizadoService = momentoRealizadoService;
        this.docenteRepo = docenteRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.componenteRepo = componenteRepo;
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

    public Boolean deleteMomento(MomentoRequest momentoRequest){
        Optional<Componente> optionalComponente = findComponenteByType(momentoRequest.getCadeiraNome(),momentoRequest.getAno(),momentoRequest.getComp());
        if (optionalComponente.isPresent()){
            for (Momento m:optionalComponente.get().getMomentos()) {
                if (m.getDesignation().equals(momentoRequest.getMomento().getDesignation())){
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
        Optional<Iterable<MomentoRealizado>> optionalMomentoRealizado =momentoRealizadoRepo.findAllByMomento(m);
        if (optionalMomentoRealizado.isPresent()){
            Set<MomentoRealizado> auxMrSet = new HashSet<>();
            for (MomentoRealizado mr : optionalMomentoRealizado.get()) {
                auxMrSet.add(mr);
            }
            while(!auxMrSet.isEmpty()){
                Iterator<MomentoRealizado> momentosRealizados = optionalMomentoRealizado.get().iterator();
                MomentoRealizado mr = momentosRealizados.next();
                auxMrSet.remove(mr);
                momentoRealizadoService.delete(mr);
            }
        }
        momentoRepo.delete(m);
    }

    public Optional<Momento> createMomento(String id, String cadeira, int ano, String comp, Momento momento){
        if (momento.getDesignation() == null || momento.getPesoAvaliacao()<=0){
            return Optional.empty();
        }
        Float pesoMomento = 0.0f;
        Float pesoRa = 0.0f;
        for (Questao q : momento.getQuestoes()) {
            pesoMomento += q.getPesoMomento();
            pesoRa += q.getPesoRA();
            if (pesoRa>1.0f || pesoMomento >1.0f){
                return Optional.empty();
            }
        }
        Optional<Componente> optionalComponente = findComponenteByType(cadeira,ano,comp);
        Optional<Docente> optionalDocente = docenteRepo.findByCode(id);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()){
            if (optionalDocente.get().getComponentes().contains(optionalComponente.get())){
                float peso = 0.0f;
                Componente componente=optionalComponente.get();
                for (Momento m : componente.getMomentos()) {
                    peso = peso + m.getPesoAvaliacao();
                    if (m.getDesignation().equals(momento.getDesignation()) || peso>=1.0f){
                        return Optional.empty();
                    }
                }
                componente.addMomento(momento);
                for (Aluno a : componente.getAlunos()) {
                    MomentoRealizado mr = new MomentoRealizadoBuilder().setAluno(a).setMomento(momento).setGrade(0.0f).build();
                    for (Questao q : momento.getQuestoes()) {
                        QuestaoRespondida qr = new QuestaoRespondidaBuilder().setQuestao(q).setMomento(mr).build();
                    }
                    momentoRealizadoRepo.save(mr);
                }
                momentoRepo.save(momento);
                return Optional.of(momento);
            }
        }
        return Optional.empty();
    }

    public Optional<Momento> updateMomento(MomentoRequest momentoRequest){
        if (momentoRequest.getMomento() == null ||
                momentoRequest.getAno() <= 0 ||
                momentoRequest.getDocenteNumero() == null ||
                momentoRequest.getCadeiraNome() == null ||
                momentoRequest.getComp() == null ||
                momentoRequest.getMomentoNome() == null){
            return Optional.empty();
        }
        Optional<Componente> optionalComponente = findComponenteByType(momentoRequest.getCadeiraNome(),momentoRequest.getAno(), momentoRequest.getComp());
        Optional<Docente> optionalDocente = docenteRepo.findByCode(momentoRequest.getDocenteNumero());
        if (optionalComponente.isPresent() && optionalDocente.isPresent()){
            Componente componente = optionalComponente.get();
            Docente docente = optionalDocente.get();
            if (docente.getComponentes().contains(componente)){
                Momento upMomento = momentoRequest.getMomento();
                for (Momento m : componente.getMomentos()) {
                    if (m.getDesignation().equals(momentoRequest.getMomentoNome())){
                        if (upMomento.getDesignation()!=null){
                            Boolean checkName = true;
                            for (Momento aux : componente.getMomentos()) {
                                if (aux.getDesignation().equals(upMomento.getDesignation())){
                                    checkName = false;
                                    break;
                                }
                            }
                            if (checkName){
                                m.setDesignation(upMomento.getDesignation());
                            }
                        }
                        if (upMomento.getPesoAvaliacao() != null && upMomento.getPesoAvaliacao() != 0.0f){
                            Float peso = 0.0f;
                            for (Momento aux:componente.getMomentos()) {
                                peso+=aux.getPesoAvaliacao();
                            }
                            peso-=m.getPesoAvaliacao();
                            if (upMomento.getPesoAvaliacao()<= (1-peso)){
                                m.setPesoAvaliacao(upMomento.getPesoAvaliacao());
                            }
                        }
                        momentoRepo.save(m);
                        return Optional.of(m);
                    }
                }
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

