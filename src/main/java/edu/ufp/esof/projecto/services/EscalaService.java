package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Escala;
import edu.ufp.esof.projecto.models.EscalaRequest;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.EscalaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class EscalaService {

    private EscalaRepo escalaRepo;
    private CadeiraRepo cadeiraRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public EscalaService(EscalaRepo escalaRepo, CadeiraRepo cadeiraRepo) {
        this.escalaRepo = escalaRepo;
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
    
    public Optional<Escala> createEscala(String cadeira, Escala escala) {
        if(escala.getDesignation()==null){
            return Optional.empty();
        }
        Float nota = 0.0f;
        Optional<Cadeira> optionalCadeira=cadeiraRepo.findByDesignation(cadeira);
        if(optionalCadeira.isPresent()){
            Cadeira cadeira1=optionalCadeira.get();
            for (Escala e: cadeira1.getEscalas()) {
                if(e.getDesignation().equals(escala.getDesignation())){
                    return Optional.empty();
                }
            }
            cadeira1.addEscala(escala);
            escalaRepo.save(escala);
            return Optional.of(escala);
        }
        return Optional.empty();
    }

    public Optional<Escala> updateEscala(EscalaRequest escalaRequest){
        if(escalaRequest.getEscala() == null ||
                escalaRequest.getCadeiraNome() == null ||
                escalaRequest.getDesignation() == null){
            return Optional.empty();
        }
        Optional<Cadeira> optionalCadeira=cadeiraRepo.findByDesignation(escalaRequest.getCadeiraNome());
        if(optionalCadeira.isPresent()) {
            Cadeira cadeira = optionalCadeira.get();
            Escala upescala = escalaRequest.getEscala();
            if (upescala.getDesignation() != null) {
                for (Escala aux : cadeira.getEscalas()) {
                    if (aux.getDesignation().equals(escalaRequest.getDesignation())) {
                        aux.setDesignation(upescala.getDesignation());
                        if(upescala.getNota()!=null && upescala.getNota()!=0.0f){
                            aux.setNota(upescala.getNota());
                        }
                        escalaRepo.save(aux);
                        return Optional.of(aux);
                    }
                }
            }
        }
        return Optional.empty();
    }


    public Optional<Escala> update(Cadeira c, Escala old, Escala newCr){
        boolean check = true;
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
        escalaRepo.save(old);
        return Optional.of(old);
    }

    public Boolean deleteEscala(EscalaRequest escalaRequest){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(escalaRequest.getCadeiraNome());
        if (optionalCadeira.isPresent()){
            for (Escala e : optionalCadeira.get().getEscalas()) {
                if (e.getDesignation().equals(escalaRequest.getEscala().getDesignation())){
                    optionalCadeira.get().getEscalas().remove(e);
                    delete(e);
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
                Iterator<Escala> escalas = optionalCadeira.get().getEscalas().iterator();
                Escala c=escalas.next();
                optionalCadeira.get().getEscalas().remove(c);
                escalaRepo.delete(c);
            }
        }
    }

    public void delete(Escala e){
        escalaRepo.delete(e);
    }
}
