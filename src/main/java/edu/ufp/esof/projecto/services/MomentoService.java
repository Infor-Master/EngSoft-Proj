package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.repositories.MomentoRealizadoRepo;
import edu.ufp.esof.projecto.repositories.MomentoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MomentoService {

    private MomentoRepo momentoRepo;
    private MomentoRealizadoRepo momentoRealizadoRepo;

    @Autowired
    public MomentoService(MomentoRepo momentoRepo, MomentoRealizadoRepo momentoRealizadoRepo) {
        this.momentoRepo = momentoRepo;
        this.momentoRealizadoRepo = momentoRealizadoRepo;
    }


    public Set<Momento> findAll(){
        Set<Momento> Momentos=new HashSet<>();
        for(Momento m:this.momentoRepo.findAll()){
            Momentos.add(m);
        }
        return Momentos;
    }

    public Optional<Momento> findByDesignation(String designation){
        Optional<Momento> optionalMomento = Optional.empty();
        for(Momento m:this.momentoRepo.findAll()){
            if (m.getDesignation().compareTo(designation) == 0){
                optionalMomento = Optional.of(m);
                break;
            }
        }
        return optionalMomento;
    }

    public Set<MomentoRealizado> findAllByMomento(Momento momento){
        Set<MomentoRealizado> momentosRealizados=new HashSet<>();
        for (MomentoRealizado mr:this.momentoRealizadoRepo.findAll()) {
            if(mr.getMomento().getId().compareTo(momento.getId())==0){
                momentosRealizados.add(mr);
            }
        }
        return momentosRealizados;
    }

    public Optional<Momento> createMomento(Momento momento){
        Optional<Momento> optionalMomento=this.momentoRepo.findByDesignation(momento.getDesignation());
        if(optionalMomento.isPresent()){
            return Optional.empty();
        }
        Momento createdMomento=this.momentoRepo.save(momento);
        return Optional.of(createdMomento);
    }

    public Optional<Momento> updateMomento(String designation, Momento m){
        Optional<Momento> optionalMomento = this.momentoRepo.findByDesignation(designation);
        if(optionalMomento.isPresent()){
            momentoRepo.save(m);
            return optionalMomento;
        }
        return Optional.empty();
    }

    public Boolean deleteMomento(String designation){
        Optional<Momento> optionalMomento = this.momentoRepo.findByDesignation(designation);
        if (optionalMomento.isPresent()){
            for (MomentoRealizado mr:this.findAllByMomento(optionalMomento.get())) {
                momentoRealizadoRepo.delete(mr);
            }
            momentoRepo.delete(optionalMomento.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        for (Momento m:this.momentoRepo.findAll()) {
            for (MomentoRealizado mr:this.findAllByMomento(m)) {
                momentoRealizadoRepo.delete(mr);
            }
        }
        momentoRepo.deleteAll();
    }
}
