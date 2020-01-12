package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.models.QuestaoRespondida;
import edu.ufp.esof.projecto.repositories.MomentoRealizadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MomentoRealizadoService {

    private MomentoRealizadoRepo momentoRealizadoRepo;
    private QuestaoRespondidaService questaoRespondidaService;

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

    /**
     *  Boolean não usado ainda mas poderá ser necessário
     */
    public Boolean deleteMomentoRealizado(Long id){
        Optional<MomentoRealizado> optionalMomentoRealizado=this.momentoRealizadoRepo.findById(id);
        if(optionalMomentoRealizado.isPresent()){
            for (QuestaoRespondida qr:optionalMomentoRealizado.get().getQuestoes()) {
                questaoRespondidaService.deleteQuestaoRespondida(qr.getId());
            }
            momentoRealizadoRepo.delete(optionalMomentoRealizado.get());
            return true;
        }
        return false;
    }

    public void delete(MomentoRealizado mr){
        if (mr.getAluno() != null){
            mr.getAluno().getMomentos().remove(mr);
            mr.setAluno(null);
        }
        for (QuestaoRespondida qr : mr.getQuestoes()) {
            questaoRespondidaService.delete(qr);
        }
        momentoRealizadoRepo.delete(mr);
    }
}
