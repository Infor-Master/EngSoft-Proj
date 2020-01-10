package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.repositories.MomentoRealizadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MomentoRealizadoService {

    private MomentoRealizadoRepo momentoRealizadoRepo;

    public Set<MomentoRealizado> findAll(){
        Set<MomentoRealizado> MRs=new HashSet<>();
        for(MomentoRealizado momento:this.momentoRealizadoRepo.findAll()){
            MRs.add(momento);
        }
        return MRs;
    }

    public Optional<MomentoRealizado> findById(Long id){
        Optional<MomentoRealizado> optionalMR = Optional.empty();
        for(MomentoRealizado momento:this.momentoRealizadoRepo.findAll()){
            if (momento.getId().compareTo(id) == 0){
                optionalMR = Optional.of(momento);
                break;
            }
        }
        return optionalMR;
    }

    public Optional<MomentoRealizado> createMomentoRealizado(MomentoRealizado momentoRealizado){
        Optional<MomentoRealizado> optionalMR=this.momentoRealizadoRepo.findById(momentoRealizado.getId());
        if(optionalMR.isPresent()){
            return Optional.empty();
        }
        MomentoRealizado createdMR=this.momentoRealizadoRepo.save(momentoRealizado);
        return Optional.of(createdMR);
    }

    public Optional<MomentoRealizado> updateMomentoRealizado(Long id, MomentoRealizado momento){
        Optional<MomentoRealizado> optionalMR = this.momentoRealizadoRepo.findById(id);
        if(optionalMR.isPresent()){
            momentoRealizadoRepo.save(momento);
            return optionalMR;
        }
        return Optional.empty();
    }

    public Boolean deleteMomentoRealizado(Long id){
        Optional<MomentoRealizado> optionalMR = this.momentoRealizadoRepo.findById(id);
        if (optionalMR.isPresent()){
            momentoRealizadoRepo.delete(optionalMR.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        momentoRealizadoRepo.deleteAll();
    }}
