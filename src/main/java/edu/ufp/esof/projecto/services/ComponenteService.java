package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.ComponenteRepo;
import edu.ufp.esof.projecto.services.filters.Componente.FilterComponenteObject;
import edu.ufp.esof.projecto.services.filters.Componente.FilterComponenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ComponenteService {

    private ComponenteRepo componenteRepo;
    private CadeiraRepo cadeiraRepo;
    private CadeiraService cadeiraService;
    private FilterComponenteService filterService;

    @Autowired
    public ComponenteService(ComponenteRepo componenteRepo, CadeiraRepo cadeiraRepo, CadeiraService cadeiraService, FilterComponenteService filterService) {
        this.componenteRepo = componenteRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.cadeiraService = cadeiraService;
        this.filterService = filterService;
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
        Optional<Cadeira> optionalCadeira = cadeiraService.findByName(cadeira);
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

    //tem um bug, adiciona um novo objeto a base de dados em vez de o substituir
    public Optional<Componente> updateComponente(String cadeira, int ano, String type, Componente componente){
        Optional<Componente> optionalComponente = findByType(cadeira,ano,type);
        if(optionalComponente.isPresent()){
            componenteRepo.save(componente);

        }
        return optionalComponente;
    }


    public Boolean deleteComponente(String cadeira, int ano, String type){
        Optional<Componente> optionalComponente = findByType(cadeira,ano,type);
        //Optional<Componente> optionalComponente=this.componenteRepo.findByType(type);
        if(optionalComponente.isPresent()){
            for (Componente c : optionalComponente.get().getOferta().getComponentes()) {
               if (c.getType().compareTo(type) == 0){
                   optionalComponente.get().getOferta().getComponentes().remove(c);
                   break;
               }
            }
            optionalComponente.get().setOferta(null);
            componenteRepo.delete(optionalComponente.get());
            return true;
        }
        return false;
    }

    public void deleteAll(String cadeira, int ano){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno() == ano){
                    for (Componente c : o.getComponentes()) {
                        o.setComponentes(new HashSet<>());
                        c.setOferta(null);
                        componenteRepo.deleteById(c.getId());
                    }
                }
            }
        }
    }
}
