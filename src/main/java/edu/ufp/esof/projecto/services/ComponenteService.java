package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.ComponenteRepo;
import edu.ufp.esof.projecto.repositories.OfertaRepo;
import edu.ufp.esof.projecto.services.filters.Componente.FilterComponenteObject;
import edu.ufp.esof.projecto.services.filters.Componente.FilterComponenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ComponenteService {

    private ComponenteRepo componenteRepo;
    private CadeiraRepo cadeiraRepo;
    private OfertaRepo ofertaRepo;
    private MomentoService momentoService;
    private FilterComponenteService filterService;

    @Autowired
    public ComponenteService(ComponenteRepo componenteRepo, CadeiraRepo cadeiraRepo, OfertaRepo ofertaRepo, MomentoService momentoService, FilterComponenteService filterService) {
        this.componenteRepo = componenteRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.momentoService = momentoService;
        this.filterService = filterService;
        this.ofertaRepo = ofertaRepo;
    }

    public Set<Componente> filterComponente(Map<String, String> searchParams){
        FilterComponenteObject filterComponenteObject= new FilterComponenteObject(searchParams);
        Set<Componente> componentes=this.findAll();
        return this.filterService.filter(componentes, filterComponenteObject);
    }

    public Set<Componente> findAll() {
        Set<Componente> componentes=new HashSet<>();
        for (Componente c:this.componenteRepo.findAll()) {
            componentes.add(c);
        }
        return componentes;
    }

    public Set<Componente> findAll(String cadeira, int ano) {
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        Set<Componente> componentes =new HashSet<>();
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno() == ano){
                    componentes.addAll(o.getComponentes());
                    return componentes;
                }
            }
        }
        return componentes;
    }

    public Optional<Componente> findByType(String cadeira, int ano, String type) {
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

    public Optional<Componente> createComponente(String cadeira, int ano, Componente componente) {
        Optional<Componente> optionalComponente= findByType(cadeira,ano,componente.getType());
        if(optionalComponente.isPresent()){
            return Optional.empty();
        }
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno()==ano){
                    o.getComponentes().add(componente);
                    componente.setOferta(o);
                    Componente createdComponente=this.componenteRepo.save(componente);
                    return Optional.of(createdComponente);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Componente> updateComponente(String cadeira, int ano, String type, Componente componente){
        Optional<Componente> optionalComponente = findByType(cadeira,ano,type);
        if(optionalComponente.isPresent()){
            return update(optionalComponente.get(),componente);

        }
        return optionalComponente;
    }

    public Optional<Componente> update(Componente old, Componente newComp){
        if (newComp.getType() != null && old.getType().compareTo(newComp.getType()) != 0){
            Optional<Oferta> o = ofertaRepo.findById(old.getOferta().getId());
            if (o.isPresent()){
                for (Componente c : o.get().getComponentes()) {
                    if (c.getType().compareTo(newComp.getType()) == 0){
                        return Optional.of(old);
                    }
                }
                old.setType(newComp.getType());
                componenteRepo.save(old);
            }
        }
        return Optional.of(old);
    }


    public Boolean deleteComponente(String cadeira, int ano, String type){
        Optional<Componente> optionalComponente = findByType(cadeira,ano,type);
        if(optionalComponente.isPresent()){
            for (Componente c : optionalComponente.get().getOferta().getComponentes()) {
               if (c.getType().compareTo(type) == 0){
                   delete(c);
                   return true;
                   //optionalComponente.get().getOferta().getComponentes().remove(c);
                   //break;
               }
            }
           // optionalComponente.get().setOferta(null);
            //componenteRepo.delete(optionalComponente.get());
            //return true;
        }
        return false;
    }

    public void deleteAll(String cadeira, int ano){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno() == ano){
                    for (Componente c : o.getComponentes()) {
                        delete(c);
                        //o.setComponentes(new HashSet<>());
                        //c.setOferta(null);
                        //componenteRepo.deleteById(c.getId());
                    }
                }
            }
        }
    }

    public void delete(Componente c){
        if (c.getDocente() != null){
            c.getDocente().getComponentes().remove(c);
            c.setDocente(null);
        }
        while(!c.getAlunos().isEmpty()){
            Iterator<Aluno> alunos = c.getAlunos().iterator();
            Aluno a = alunos.next();
            a.getComponentes().remove(c);
            c.getAlunos().remove(a);
        }
        while(!c.getMomentos().isEmpty()){
            Iterator<Momento> momentos = c.getMomentos().iterator();
            Momento m = momentos.next();
            momentoService.delete(m);
        }
        /*
        for (Aluno a : c.getAlunos()) {
            a.getComponentes().remove(c);
            c.getAlunos().remove(a);
        }
        for (Momento m : c.getMomentos()) {
            momentoService.delete(m);
        }*/
        if (c.getOferta() != null){
            c.getOferta().getComponentes().remove(c);
            c.setOferta(null);
        }
        componenteRepo.delete(c);
    }
}
