package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.CriterioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class CriterioService {

    private CriterioRepo criterioRepo;
    private CadeiraRepo cadeiraRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public CriterioService(CriterioRepo criterioRepo, CadeiraRepo cadeiraRepo) {
        this.criterioRepo = criterioRepo;
        this.cadeiraRepo = cadeiraRepo;
    }

    public Set<Criterio> findAll(String cadeira) {
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
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
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
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
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Criterio c : optionalCadeira.get().getCriterios()) {
                if (c.getDesignation().compareTo(designation)==0){
                    return update(optionalCadeira.get(),c,criterio);
                }
            }
        }
        return Optional.empty();
    }


    public Optional<Criterio> update(Cadeira c, Criterio old, Criterio newCr){
        Boolean check = true;
        if (newCr.getDesignation() != null && old.getDesignation().compareTo(newCr.getDesignation()) != 0){
            for (Criterio cr : c.getCriterios()) {
                if(cr.getDesignation().compareTo(newCr.getDesignation()) == 0){
                    check=false;
                    break;
                }
            }
            if (check){
                old.setDesignation(newCr.getDesignation());
            }
            check = true;
        }
        if (newCr.getNota() != old.getNota()){
            for (Criterio cr : c.getCriterios()) {
                if (cr.getNota() == newCr.getNota()){
                    check = false;
                    break;
                }
            }
            if (check){
                old.setNota(newCr.getNota());
            }
        }
        criterioRepo.save(old);
        return Optional.of(old);
    }

    public Boolean deleteCriterio(String cadeira, String designation){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
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
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if(optionalCadeira.isPresent()){
            while(!optionalCadeira.get().getCriterios().isEmpty()){
                Iterator<Criterio> criterios = optionalCadeira.get().getCriterios().iterator();
                Criterio c=criterios.next();
                optionalCadeira.get().getCriterios().remove(c);
                criterioRepo.delete(c);
            }
        }
    }

    public void delete(Criterio c){
        criterioRepo.delete(c);
    }
}
