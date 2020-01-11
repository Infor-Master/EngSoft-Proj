package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.repositories.CriterioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class CriterioService {

    private CriterioRepo criterioRepo;
    private CadeiraService cadeiraService;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public CriterioService(CriterioRepo criterioRepo, CadeiraService cadeiraService) {
        this.criterioRepo = criterioRepo;
        this.cadeiraService = cadeiraService;
    }

    public Set<Criterio> findAll(String cadeira) {
        Optional<Cadeira> optionalCadeira = cadeiraService.findByName(cadeira);
        Set<Criterio> criterios=new HashSet<>();
        if (optionalCadeira.isPresent()){
            for(Criterio c: optionalCadeira.get().getCriterios()){
                criterios.add(c);
            }
        }
        return criterios;
    }

    public Optional<Criterio> findByDesignation(String cadeira, String designation) {
        Set<Criterio> criterios = findAll(cadeira);
        for (Criterio c : criterios) {
            if (c.getDesignation().compareTo(designation) == 0){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Criterio> createCriterio(String cadeira, Criterio criterio) {
        Optional<Cadeira> optionalCadeira = cadeiraService.findByName(cadeira);
        if (optionalCadeira.isPresent()){
            for (Criterio c : optionalCadeira.get().getCriterios()) {
                if (c.getDesignation().compareTo(criterio.getDesignation())==0){
                    return Optional.empty();
                }
            }
            optionalCadeira.get().getCriterios().add(criterio);
            this.criterioRepo.save(criterio);
            return Optional.of(criterio);
        }
        return Optional.empty();
    }

    public Optional<Criterio> updateCriterio(String cadeira, String designation, Criterio criterio){
        Optional<Cadeira> optionalCadeira = cadeiraService.findByName(cadeira);
        if (optionalCadeira.isPresent()){
            for (Criterio c : optionalCadeira.get().getCriterios()) {
                if (c.getDesignation().compareTo(designation)==0){
                    c=criterio;
                    criterioRepo.save(criterio);
                    return Optional.of(criterio);
                }
            }
        }
        return Optional.empty();
    }

    public Boolean deleteCriterio(String cadeira, String designation){
        Optional<Cadeira> optionalCadeira = cadeiraService.findByName(cadeira);
        if (optionalCadeira.isPresent()){
            for (Criterio c : optionalCadeira.get().getCriterios()) {
                if (c.getDesignation().compareTo(designation) == 0){
                    optionalCadeira.get().getCriterios().remove(c);
                    criterioRepo.delete(c);
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteAll(String cadeira){
        Optional<Cadeira> optionalCadeira = cadeiraService.findByName(cadeira);
        if(optionalCadeira.isPresent()){
            while(!optionalCadeira.get().getCriterios().isEmpty()){
                Iterator<Criterio> criterios = optionalCadeira.get().getCriterios().iterator();
                Criterio c=criterios.next();
                optionalCadeira.get().getCriterios().remove(c);
                criterioRepo.delete(c);
            }
        }
    }
}
