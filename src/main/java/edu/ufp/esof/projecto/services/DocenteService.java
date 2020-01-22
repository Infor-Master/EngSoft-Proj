package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.ComponenteRepo;
import edu.ufp.esof.projecto.repositories.DocenteRepo;
import edu.ufp.esof.projecto.repositories.MomentoRepo;
import edu.ufp.esof.projecto.services.filters.Docente.FilterDocenteObject;
import edu.ufp.esof.projecto.services.filters.Docente.FilterDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocenteService {

    private DocenteRepo docenteRepo;
    private ComponenteRepo componenteRepo;
    private MomentoRepo momentoRepo;
    private FilterDocenteService filterService;
    private ComponenteService componenteService;
    private AlunoService alunoService;
    private MomentoRealizadoService momentoRealizadoService;

    @Autowired
    public DocenteService(DocenteRepo docenteRepo, AlunoService alunoService, ComponenteRepo componenteRepo, MomentoRepo momentoRepo, FilterDocenteService filterService, ComponenteService componenteService, MomentoRealizadoService momentoRealizadoService) {
        this.docenteRepo = docenteRepo;
        this.componenteRepo = componenteRepo;
        this.momentoRepo = momentoRepo;
        this.filterService = filterService;
        this.componenteService = componenteService;
        this.momentoRealizadoService = momentoRealizadoService;
        this.alunoService = alunoService;
    }


    public Set<Docente> filterDocente(Map<String, String> searchParams){
        FilterDocenteObject filterDocenteObject= new FilterDocenteObject(searchParams);
        Set<Docente> docentes=this.findAll();
        return this.filterService.filter(docentes, filterDocenteObject);
    }
    
    public Set<Docente> findAll() {
        Set<Docente> docentes=new HashSet<>();
        for(Docente d:this.docenteRepo.findAll()){
            docentes.add(d);
        }
        return docentes;
    }

    public Optional<Docente> findByNumber(String id) {
        Optional<Docente> optionalDocente = Optional.empty();
        for(Docente d:this.docenteRepo.findAll()){
            if (d.getCode().compareTo(id) == 0){
                optionalDocente = Optional.of(d);
                break;
            }
        }
        return optionalDocente;
    }

    public Optional<Docente> createDocente(Docente docente) {
        Optional<Docente> optionalDocente=this.docenteRepo.findByCode(docente.getCode());
        if(optionalDocente.isPresent()){
            return Optional.empty();
        }
        Docente createdDocente=this.docenteRepo.save(docente);
        return Optional.of(createdDocente);
    }

    public Optional<Docente> updateDocente(String code, Docente docente){
        Optional<Docente> optionalDocente=this.docenteRepo.findByCode(code);
        if(optionalDocente.isPresent()){
            return update(optionalDocente.get(),docente);
        }
        return Optional.empty();
    }

    public Optional<Docente> update(Docente old, Docente newDoc){
        if (newDoc.getName() != null && old.getName().compareTo(newDoc.getName()) != 0){
            old.setName(newDoc.getName());
        }
        if (newDoc.getCode() != null && old.getCode().compareTo(newDoc.getCode()) != 0){
            Optional<Docente> test = docenteRepo.findByCode(newDoc.getCode());
            if (test.isEmpty()){
                old.setCode(newDoc.getCode());
            }
        }
        docenteRepo.save(old);
        return Optional.of(old);
    }

    public Boolean deleteDocente(String code){
        Optional<Docente> optionalDocente=this.docenteRepo.findByCode(code);
        if(optionalDocente.isPresent()){
            delete(optionalDocente.get());
            //docenteRepo.delete(optionalDocente.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        for (Docente d : docenteRepo.findAll()) {
            delete(d);
        }
    }

    public void delete(Docente d){
        for (Componente c : d.getComponentes()) {
            c.setDocente(null);
            d.getComponentes().remove(c);
        }
        docenteRepo.delete(d);
    }

    public boolean associateDocenteComponente(String id, String cadeira, int ano, String comp){
        Optional<Docente> optionalDocente = docenteRepo.findByCode(id);
        Optional<Componente> optionalComponente = componenteService.findByType(cadeira,ano,comp);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()){
            optionalDocente.get().associateDocenteComponente(optionalComponente.get());
            docenteRepo.save(optionalDocente.get());
            componenteRepo.save(optionalComponente.get());
            return true;
        }
        return false;
    }

    public boolean desassociateDocenteComponente(String id, String cadeira, int ano, String comp){
        Optional<Docente> optionalDocente = docenteRepo.findByCode(id);
        Optional<Componente> optionalComponente = componenteService.findByType(cadeira,ano,comp);
        if (optionalComponente.isPresent() && optionalDocente.isPresent()){
            optionalDocente.get().desassociateDocenteComponente(optionalComponente.get());
            docenteRepo.save(optionalDocente.get());
            componenteRepo.save(optionalComponente.get());
            return true;
        }
        return false;
    }

    public Optional<Set<Escala>> notasFinaisComponente(String cadeira, NotaRequest nr){
        Optional<Docente> optionalDocente = docenteRepo.findByCode(nr.getDocenteNumero());
        if (optionalDocente.isPresent()){
            for (Componente c : optionalDocente.get().getComponentes()) {
                if (c.getOferta().getCadeira().getDesignation().equals(cadeira)){
                    Set<Escala> notasfinais = new HashSet<>();
                    for (Aluno a : c.getAlunos()) {
                        float nota = 0.0f;
                        for (MomentoRealizado mr : a.getMomentos()) {
                            if (mr.getMomento().getComponente().getId().equals(c.getId())){
                                nota += (mr.nota()*mr.getMomento().getPesoAvaliacao());
                            }
                        }
                        Escala e = new Escala(a.getName() + " - " + c.getOferta().getCadeira().getDesignation(), nota);
                        notasfinais.add(e);
                    }
                    return Optional.of(notasfinais);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Set<Set<Escala>>> notasFinais(NotaRequest nr){
        Optional<Docente> optionalDocente = docenteRepo.findByCode(nr.getDocenteNumero());
        if (optionalDocente.isPresent()){
            Set<Set<Escala>> notas = new HashSet<>();
            for (Componente c : optionalDocente.get().getComponentes()) {
                Optional<Set<Escala>> optionalNotas = notasFinaisComponente(c.getOferta().getCadeira().getDesignation(), nr);
                if (optionalNotas.isPresent()){
                    notas.add(optionalNotas.get());
                }
            }
            return Optional.of(notas);
        }
        return Optional.empty();
    }

    public Optional<Set<Escala>> notasMomento(NotaRequest nr){
        if (nr.getDocenteNumero()==null || nr.getComponente()==null || nr.getCadeiraNome() == null){
            return Optional.empty();
        }
        Optional<Docente> optionalDocente = docenteRepo.findByCode(nr.getDocenteNumero());
        Optional<Componente> optionalComponente = componenteService.findByType(nr.getCadeiraNome(), nr.getAno(), nr.getComponente());
        if (optionalDocente.isPresent() && optionalComponente.isPresent() && optionalDocente.get().getComponentes().contains(optionalComponente.get())){
            Set<Escala> notas = new HashSet<>();
            for (Momento m : optionalComponente.get().getMomentos()) {
                for (Aluno a : optionalComponente.get().getAlunos()) {
                    for (MomentoRealizado mr : a.getMomentos()) {
                        if (mr.getMomento().getId().equals(m.getId())){
                            Optional<Set<Escala>> auxOptional = alunoService.notasCadeira(a.getCode(), nr.getCadeiraNome(),nr.getAno(), nr.getComponente(),0);
                            if (auxOptional.isPresent()){
                                for (Escala eAux : auxOptional.get()) {
                                    if (eAux.getDesignation().equals(m.getDesignation())){
                                        Escala e = new Escala(a.getName() + " - " + m.getDesignation(), eAux.getNota());
                                        notas.add(e);
                                        break;
                                    }
                                }

                            }
                            break;
                        }
                    }
                }
            }
            return Optional.of(notas);
        }
        return Optional.empty();
    }
}
