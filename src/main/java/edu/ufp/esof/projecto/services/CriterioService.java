package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.repositories.CriterioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CriterioService {

    private CriterioRepo criterioRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public CriterioService(CriterioRepo criterioRepo) {
        this.criterioRepo = criterioRepo;
    }

    public Set<Criterio> findAll() {
        Set<Criterio> criterios=new HashSet<>();
        for(Criterio c:this.criterioRepo.findAll()){
            criterios.add(c);
        }
        return criterios;
    }

    public Optional<Criterio> findByDesignation(String designation) {
        Optional<Criterio> optionalCriterio = Optional.empty();
        for(Criterio c:this.criterioRepo.findAll()){
            if (c.getDesignation().compareTo(designation) == 0){
                optionalCriterio = Optional.of(c);
                break;
            }
        }
        return optionalCriterio;
    }

    public Optional<Criterio> createCriterio(Criterio criterio) {
        Optional<Criterio> optionalCriterio=this.criterioRepo.findByDesignation(criterio.getDesignation());
        if(optionalCriterio.isPresent()){
            return Optional.empty();
        }
        Criterio createdCriterio=this.criterioRepo.save(criterio);
        return Optional.of(createdCriterio);
    }

    public Optional<Criterio> updateCriterio(String designation, Criterio criterio){
        Optional<Criterio> optionalCriterio=this.criterioRepo.findByDesignation(designation);
        if(optionalCriterio.isPresent()){
            criterioRepo.save(criterio);
            return optionalCriterio;
        }
        return Optional.empty();
    }

    public Boolean deleteCriterio(String designation){
        Optional<Criterio> optionalCriterio=this.criterioRepo.findByDesignation(designation);
        if(optionalCriterio.isPresent()){
            criterioRepo.delete(optionalCriterio.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        criterioRepo.deleteAll();
    }
}
