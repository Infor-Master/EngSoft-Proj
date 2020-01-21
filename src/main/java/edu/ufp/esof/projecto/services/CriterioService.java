package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Escala;
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

    public Set<Escala> findAll(String cadeira) {
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        Set<Escala> escalas =new HashSet<>();
        if (optionalCadeira.isPresent()){
            for(Escala c: optionalCadeira.get().getEscalas()){
                escalas.add(c);
            }
        }
        return escalas;
    }

    public Optional<Escala> findByDesignation(String cadeira, String designation) {
        Set<Escala> escalas = findAll(cadeira);
        for (Escala c : escalas) {
            if (c.getDesignation().compareTo(designation) == 0){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Escala> createCriterio(String cadeira, Escala escala) {
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Escala c : optionalCadeira.get().getEscalas()) {
                if (c.getDesignation().compareTo(escala.getDesignation())==0){
                    return Optional.empty();
                }
            }
            optionalCadeira.get().getEscalas().add(escala);
            this.criterioRepo.save(escala);
            return Optional.of(escala);
        }
        return Optional.empty();
    }

    public Optional<Escala> updateCriterio(String cadeira, String designation, Escala escala){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Escala c : optionalCadeira.get().getEscalas()) {
                if (c.getDesignation().compareTo(designation)==0){
                    return update(optionalCadeira.get(),c, escala);
                }
            }
        }
        return Optional.empty();
    }


    public Optional<Escala> update(Cadeira c, Escala old, Escala newCr){
        Boolean check = true;
        if (newCr.getDesignation() != null && old.getDesignation().compareTo(newCr.getDesignation()) != 0){
            for (Escala cr : c.getEscalas()) {
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
            for (Escala cr : c.getEscalas()) {
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
            for (Escala c : optionalCadeira.get().getEscalas()) {
                if (c.getDesignation().compareTo(designation) == 0){
                    optionalCadeira.get().getEscalas().remove(c);
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
            while(!optionalCadeira.get().getEscalas().isEmpty()){
                Iterator<Escala> criterios = optionalCadeira.get().getEscalas().iterator();
                Escala c=criterios.next();
                optionalCadeira.get().getEscalas().remove(c);
                criterioRepo.delete(c);
            }
        }
    }

    public void delete(Escala c){
        criterioRepo.delete(c);
    }
}
