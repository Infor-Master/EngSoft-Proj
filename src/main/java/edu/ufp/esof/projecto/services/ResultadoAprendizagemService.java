package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.ResultadoAprendizagemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ResultadoAprendizagemService {

    private ResultadoAprendizagemRepo resultadoAprendizagemRepo;
    private CadeiraRepo cadeiraRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public ResultadoAprendizagemService(ResultadoAprendizagemRepo resultadoAprendizagemRepo, CadeiraRepo cadeiraRepo) {
        this.resultadoAprendizagemRepo = resultadoAprendizagemRepo;
        this.cadeiraRepo = cadeiraRepo;
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

    public Optional<ResultadoAprendizagem> createResultadoAprendizagem(String cadeira, int ano, ResultadoAprendizagem resultadoAprendizagem){
        if(resultadoAprendizagem.getDesignation()==null){
            return Optional.empty();
        }
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if(optionalCadeira.isPresent()){
            Cadeira cadeira1=optionalCadeira.get();
            for (Oferta o:cadeira1.getOfertas()) {
                if(o.getAno()==ano){
                    for (ResultadoAprendizagem ra:o.getRas()) {
                        if(ra.getDesignation().equals(resultadoAprendizagem.getDesignation())){
                            return Optional.empty();
                        }
                    }
                    o.addRA(resultadoAprendizagem);
                    resultadoAprendizagemRepo.save(resultadoAprendizagem);
                    return Optional.of(resultadoAprendizagem);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<ResultadoAprendizagem> updateResultadoAprendizagem(RARequest raRequest){
        if(raRequest.getResultadoAprendizagem() == null ||
                raRequest.getAno() <=0 ||
                raRequest.getCadeiraNome() == null ||
                raRequest.getRANome() == null){
            return Optional.empty();
        }
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(raRequest.getCadeiraNome());
        if(optionalCadeira.isPresent()){
            Cadeira c=optionalCadeira.get();
            ResultadoAprendizagem upRA = raRequest.getResultadoAprendizagem();
            for (Oferta o:c.getOfertas()) {
                if(o.getAno().equals(raRequest.getAno())){
                    for (ResultadoAprendizagem rao:o.getRas()) {
                        if(rao.getDesignation().equals(raRequest.getRANome())){
                            if(upRA.getDesignation()!=null){
                                boolean checkName = true;
                                for (ResultadoAprendizagem aux:o.getRas()) {
                                    if(aux.getDesignation().equals(upRA.getDesignation())){
                                        checkName = false;
                                        break;
                                    }
                                }
                                if(checkName){
                                    rao.setDesignation(upRA.getDesignation());
                                }
                            }
                            resultadoAprendizagemRepo.save(rao);
                            return Optional.of(rao);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    // Falta fazer
    public Boolean deleteResultadoAprendizagem(RARequest raRequest){
        Optional<Oferta> optionalOferta = findOfertaByAno(raRequest.getCadeiraNome(), raRequest.getAno());
        if(optionalOferta.isPresent()){
            for (ResultadoAprendizagem RA:optionalOferta.get().getRas()) {
                if(RA.getDesignation().equals(raRequest.getResultadoAprendizagem().getDesignation())){
                    delete(RA);
                    return true;
                }
            }
        }
        return false;
    }

    private Optional<Oferta> findOfertaByAno(String cadeiraNome, int ano) {
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeiraNome);
        if(optionalCadeira.isPresent()){
            for (Oferta o:optionalCadeira.get().getOfertas()) {
                if (o.getAno()==ano){
                    return Optional.of(o);
                }
            }
        }
        return Optional.empty();
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
