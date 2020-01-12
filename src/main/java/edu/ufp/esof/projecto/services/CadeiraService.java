package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.services.filters.Cadeira.FilterCadeiraObject;
import edu.ufp.esof.projecto.services.filters.Cadeira.FilterCadeiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CadeiraService {

    private CadeiraRepo cadeiraRepo;
    private FilterCadeiraService filterService;
    private OfertaService ofertaService;
    private CriterioService criterioService;

    @Autowired
    public CadeiraService(CadeiraRepo cadeiraRepo, FilterCadeiraService filterService, OfertaService ofertaService, CriterioService criterioService) {
        this.cadeiraRepo = cadeiraRepo;
        this.filterService=filterService;
        this.ofertaService = ofertaService;
        this.criterioService = criterioService;
    }

    public Set<Cadeira> filterCadeira(Map<String, String> searchParams){
        FilterCadeiraObject filterCadeiraObject= new FilterCadeiraObject(searchParams);
        Set<Cadeira> cadeiras=this.findAll();
        return this.filterService.filter(cadeiras, filterCadeiraObject);
    }

    public Set<Cadeira> findAll() {
        Set<Cadeira> cadeiras=new HashSet<>();
        for(Cadeira c:this.cadeiraRepo.findAll()){
            cadeiras.add(c);
        }
        return cadeiras;
    }

    public Optional<Cadeira> findByNumber(String id) {
        Optional<Cadeira> optionalCadeira = Optional.empty();
        for(Cadeira c:this.cadeiraRepo.findAll()){
            if (c.getCode().compareTo(id) == 0){
                optionalCadeira = Optional.of(c);
                break;
            }
        }
        return optionalCadeira;
    }

    public Optional<Cadeira> findByName(String nome) {
        Optional<Cadeira> optionalCadeira = Optional.empty();
        for(Cadeira c:this.cadeiraRepo.findAll()){
            if (c.getDesignation().compareTo(nome) == 0){
                optionalCadeira = Optional.of(c);
                break;
            }
        }
        return optionalCadeira;
    }



    public Optional<Cadeira> createCadeira(Cadeira cadeira) {
        Optional<Cadeira> optionalCadeira=this.cadeiraRepo.findByCode(cadeira.getCode());
        if(optionalCadeira.isPresent()){
            return Optional.empty();
        }
        Cadeira createdCadeira=this.cadeiraRepo.save(cadeira);
        return Optional.of(createdCadeira);
    }

    public Optional<Cadeira> updateCadeira(String name, Cadeira cadeira){
        Optional<Cadeira> optionalCadeira=this.cadeiraRepo.findByDesignation(name);
        if(optionalCadeira.isPresent()){
            return update(optionalCadeira.get(),cadeira);
        }
        return Optional.empty();
    }

    public Optional<Cadeira> update(Cadeira old, Cadeira newCadeira){
        if (newCadeira.getDesignation() != null && old.getDesignation().compareTo(newCadeira.getDesignation()) != 0){
            old.setDesignation(newCadeira.getDesignation());
        }
        if (newCadeira.getCode() != null && old.getCode().compareTo(newCadeira.getCode()) != 0){
            Optional<Cadeira> test = cadeiraRepo.findByCode(newCadeira.getCode());
            if (test.isEmpty()){
                old.setCode(newCadeira.getCode());
            }
        }
        cadeiraRepo.save(old);
        return Optional.of(old);
    }

    public Boolean deleteCadeira(String code){
        Optional<Cadeira> optionalCadeira=this.cadeiraRepo.findByCode(code);
        if(optionalCadeira.isPresent()){
            delete(optionalCadeira.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        for (Cadeira c : cadeiraRepo.findAll()) {
            delete(c);
        }
    }

    public void delete(Cadeira c){
        while(!c.getOfertas().isEmpty()){
            Iterator<Oferta> ofertas = c.getOfertas().iterator();
            Oferta o = ofertas.next();
            ofertaService.delete(o);
        }
        while(!c.getCriterios().isEmpty()){
            Iterator<Criterio> criterios = c.getCriterios().iterator();
            Criterio cr = criterios.next();
            c.getCriterios().remove(cr);
            criterioService.delete(cr);
        }
        cadeiraRepo.delete(c);
    }
}
