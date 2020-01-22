package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.models.QuestaoRespondida;
import edu.ufp.esof.projecto.repositories.MomentoRealizadoRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
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

    public void delete(MomentoRealizado mr){
        if (mr.getAluno() != null){
            mr.getAluno().getMomentos().remove(mr);
            mr.setAluno(null);
        }
        if (mr.getMomento() != null){
            mr.setMomento(null);            //verificar
        }
        while(!mr.getQuestoes().isEmpty()){
            Iterator<QuestaoRespondida> questoes = mr.getQuestoes().iterator();
            QuestaoRespondida qr = questoes.next();
            questaoRespondidaService.delete(qr);
        }
        momentoRealizadoRepo.delete(mr);
    }

    public void create(Momento m){
        for (Aluno a : m.getComponente().getAlunos()) {
            MomentoRealizado mr = new MomentoRealizado(m);
            a.getMomentos().add(mr);
            mr.setAluno(a);
            momentoRealizadoRepo.save(mr);
        }
    }
}
