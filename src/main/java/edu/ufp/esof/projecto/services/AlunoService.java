package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.repositories.AlunoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;


@Service
public class AlunoService {

    private AlunoRepo alunoRepo;
    private MomentoRealizadoService momentoRealizadoService;
    private ComponenteService componenteService;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public AlunoService(AlunoRepo alunoRepo, MomentoRealizadoService momentoRealizadoService, ComponenteService componenteService) {
        this.momentoRealizadoService = momentoRealizadoService;
        this.alunoRepo = alunoRepo;
        this.componenteService = componenteService;
    }

    public Set<Aluno> findAll() {
        Set<Aluno> alunos=new HashSet<>();
        for(Aluno a:this.alunoRepo.findAll()){
            alunos.add(a);
        }
        return alunos;
    }

    public Optional<Aluno> findByNumber(String id) {
        Optional<Aluno> optionalAluno = Optional.empty();
        for(Aluno a:this.alunoRepo.findAll()){
            if (a.getCode().compareTo(id) == 0){
                optionalAluno = Optional.of(a);
                break;
            }
        }
        return optionalAluno;
    }

    public Optional<Aluno> createAluno(Aluno aluno) {
        Optional<Aluno> optionalAluno=this.alunoRepo.findByCode(aluno.getCode());
        if(optionalAluno.isPresent()){
            return Optional.empty();
        }
        Aluno createdAluno=this.alunoRepo.save(aluno);
        return Optional.of(createdAluno);
    }

    public Optional<Aluno> updateAluno(String code, Aluno aluno){
        Optional<Aluno> optionalAluno=this.alunoRepo.findByCode(code);
        if(optionalAluno.isPresent()){
            return update(optionalAluno.get(),aluno);
        }
        return Optional.empty();
    }

    public Optional<Aluno> update(Aluno old, Aluno newAluno){
        if (newAluno.getName() != null && old.getName().compareTo(newAluno.getName()) != 0){
            old.setName(newAluno.getName());
        }
        if (newAluno.getCode() != null && old.getCode().compareTo(newAluno.getCode()) != 0){
            Optional<Aluno> test = alunoRepo.findByCode(newAluno.getCode());
            if (test.isEmpty()){
                old.setCode(newAluno.getCode());
            }
        }
        alunoRepo.save(old);
        return Optional.of(old);
    }

    public Boolean deleteAluno(String code){
        Optional<Aluno> optionalAluno=this.alunoRepo.findByCode(code);
        if(optionalAluno.isPresent()){
            /*for (MomentoRealizado mr:optionalAluno.get().getMomentos()) {
                momentoRealizadoService.deleteMomentoRealizado(mr.getId());
            }
            alunoRepo.delete(optionalAluno.get());*/
            delete(optionalAluno.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        for (Aluno a:this.alunoRepo.findAll()) {
            delete(a);
        }
    }

    public void delete(Aluno a){
        /*while(!a.getComponentes().isEmpty()){
            Iterator<Componente> componentes = a.getComponentes().iterator();
            Componente c = componentes.next();
            c.getAlunos().remove(a);
            a.getComponentes().remove(c);
        }*/
        for (Componente c : a.getComponentes()) {
            c.getAlunos().remove(a);
            a.getComponentes().remove(c);
        }
        while(!a.getMomentos().isEmpty()){
            Iterator<MomentoRealizado> momentos = a.getMomentos().iterator();
            MomentoRealizado mr = momentos.next();
            momentoRealizadoService.delete(mr);
        }
        /*
        for (MomentoRealizado mr : a.getMomentos()) {
            momentoRealizadoService.delete(mr);
        }*/
        alunoRepo.delete(a);
    }

    public Optional<Set<Componente>> findComponentes(String code){
        Optional<Aluno> optionalAluno = alunoRepo.findByCode(code);
        if (optionalAluno.isPresent()){
            Set<Componente> componentes = new HashSet<>();
            for (Componente c : optionalAluno.get().getComponentes()) {
                componentes.add(c);
            }
            return Optional.of(componentes);
        }
        return Optional.empty();
    }

    public Optional<Criterio> notasRaCadeira(String id, String cadeira, int ano, String componente){
        Optional<Aluno> optionalAluno = alunoRepo.findByCode(id);
        Optional<Componente> optionalComponente = componenteService.findByType(cadeira,ano,componente);
        if (optionalAluno.isPresent() && optionalComponente.isPresent() && optionalComponente.get().getAlunos().contains(optionalAluno.get())){
            Set<Criterio> notas = new HashSet<>();
            for (Aluno a : optionalComponente.get().getAlunos()) {
                if (a.getId() == optionalAluno.get().getId()){
                    for (MomentoRealizado mr : a.getMomentos()) {
                        if (mr.getMomento().getComponente().getId() == optionalComponente.get().getId()){
                            Criterio cr = new Criterio(mr.getMomento().getComponente().getOferta().getCadeira().getDesignation() + " - " + mr.getMomento().getDesignation(), 0.0f);
                            cr.setNota(mr.notaRa());
                            return Optional.of(cr);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Set<Criterio>> notasRa(String id){
        Optional<Aluno> optionalAluno = alunoRepo.findByCode(id);
        if (optionalAluno.isPresent()){
            Set<Criterio> notas = new HashSet<>();
            for (Componente c : optionalAluno.get().getComponentes()) {
                Optional<Criterio> optionalCriterio = notasRaCadeira(id,
                        c.getOferta().getCadeira().getDesignation(),
                        c.getOferta().getAno(),
                        c.getType());
                if (optionalCriterio.isPresent()){
                    notas.add(optionalCriterio.get());
                }
            }
            return Optional.of(notas);
        }
        return Optional.empty();
    }

    public Optional<Criterio> notasCadeira(String id, String cadeira, int ano, String componente){
        Optional<Aluno> optionalAluno = alunoRepo.findByCode(id);
        Optional<Componente> optionalComponente = componenteService.findByType(cadeira,ano,componente);
        if (optionalAluno.isPresent() && optionalComponente.isPresent() && optionalComponente.get().getAlunos().contains(optionalAluno.get())){
            Set<Criterio> notas = new HashSet<>();
            for (Aluno a : optionalComponente.get().getAlunos()) {
                if (a.getId() == optionalAluno.get().getId()){
                    for (MomentoRealizado mr : a.getMomentos()) {
                        if (mr.getMomento().getComponente().getId() == optionalComponente.get().getId()){
                            Criterio cr = new Criterio(mr.getMomento().getComponente().getOferta().getCadeira().getDesignation() + " - " + mr.getMomento().getDesignation(), 0.0f);
                            cr.setNota(mr.nota());
                            return Optional.of(cr);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Set<Criterio>> notas(String id){
        Optional<Aluno> optionalAluno = alunoRepo.findByCode(id);
        if (optionalAluno.isPresent()){
            Set<Criterio> notas = new HashSet<>();
            for (Componente c : optionalAluno.get().getComponentes()) {
                Optional<Criterio> optionalCriterio = notasCadeira(id,
                        c.getOferta().getCadeira().getDesignation(),
                        c.getOferta().getAno(),
                        c.getType());
                if (optionalCriterio.isPresent()){
                    notas.add(optionalCriterio.get());
                }
            }
            return Optional.of(notas);
        }
        return Optional.empty();
    }
}