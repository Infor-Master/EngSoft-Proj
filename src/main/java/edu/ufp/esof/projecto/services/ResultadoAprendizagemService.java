package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.ResultadoAprendizagem;
import edu.ufp.esof.projecto.repositories.ResultadoAprendizagemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ResultadoAprendizagemService {

    private ResultadoAprendizagemRepo resultadoAprendizagemRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public ResultadoAprendizagemService(ResultadoAprendizagemRepo resultadoAprendizagemRepo) {
        this.resultadoAprendizagemRepo = resultadoAprendizagemRepo;
    }

    public Set<ResultadoAprendizagem> findAll(){
        Set<ResultadoAprendizagem> RAs=new HashSet<>();
        for(ResultadoAprendizagem ra:this.resultadoAprendizagemRepo.findAll()){
            RAs.add(ra);
        }
        return RAs;
    }

    public Optional<ResultadoAprendizagem> findByDesignation(String designation){
        Optional<ResultadoAprendizagem> optionalRA = Optional.empty();
        for(ResultadoAprendizagem ra:this.resultadoAprendizagemRepo.findAll()){
            if (ra.getDesignation().compareTo(designation) == 0){
                optionalRA = Optional.of(ra);
                break;
            }
        }
        return optionalRA;
    }

    public Optional<ResultadoAprendizagem> createResultadoAprendizagem(ResultadoAprendizagem resultadoAprendizagem){
        Optional<ResultadoAprendizagem> optionalRA=this.resultadoAprendizagemRepo.findByDesignation(resultadoAprendizagem.getDesignation());
        if(optionalRA.isPresent()){
            return Optional.empty();
        }
        ResultadoAprendizagem createdRA=this.resultadoAprendizagemRepo.save(resultadoAprendizagem);
        return Optional.of(createdRA);
    }

    public Optional<ResultadoAprendizagem> updateResultadoAprendizagem(String designation, ResultadoAprendizagem ra){
        Optional<ResultadoAprendizagem> optionalRA = this.resultadoAprendizagemRepo.findByDesignation(designation);
        if(optionalRA.isPresent()){
            resultadoAprendizagemRepo.save(ra);
            return optionalRA;
        }
        return Optional.empty();
    }

    // Falta fazer
    public Boolean deleteResultadoAprendizagem(String designation){
        Optional<ResultadoAprendizagem> optionalRA = this.resultadoAprendizagemRepo.findByDesignation(designation);
        if (optionalRA.isPresent()){
            resultadoAprendizagemRepo.delete(optionalRA.get());
            return true;
        }
        return false;
    }

    // Falta fazer
    public void deleteAll(){
        resultadoAprendizagemRepo.deleteAll();
    }

    public void delete(ResultadoAprendizagem ra){
        if (ra.getOferta() != null){
            ra.getOferta().getRas().remove(ra);
            ra.setOferta(null);
        }
        for (Questao q : ra.getQuestoes()) {
            q.setRa(null);
            ra.getQuestoes().remove(q);
        }
        resultadoAprendizagemRepo.delete(ra);
    }
}
