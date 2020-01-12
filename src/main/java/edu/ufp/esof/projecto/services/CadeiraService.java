package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.services.filters.Cadeira.FilterCadeiraObject;
import edu.ufp.esof.projecto.services.filters.Cadeira.FilterCadeiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CadeiraService {

    private CadeiraRepo cadeiraRepo;
    private FilterCadeiraService filterService;
    private OfertaService ofertaService;
    private CriterioService criterioService;
    // Falta o Filtro do serviço e no constructor

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
            cadeiraRepo.save(cadeira);
            return optionalCadeira;
        }
        return Optional.empty();
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
        for (Oferta o : c.getOfertas()) {
            ofertaService.delete(o);
        }
        for (Criterio cr : c.getCriterios()) {
            criterioService.delete(cr);
            c.getCriterios().remove(cr); // verificar
        }
        cadeiraRepo.delete(c);
    }
}
