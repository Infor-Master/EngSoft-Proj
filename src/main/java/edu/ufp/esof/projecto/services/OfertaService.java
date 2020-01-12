package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.models.ResultadoAprendizagem;
import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import edu.ufp.esof.projecto.repositories.OfertaRepo;
import edu.ufp.esof.projecto.services.filters.Cadeira.FilterCadeiraObject;
import edu.ufp.esof.projecto.services.filters.Oferta.FilterOfertaObject;
import edu.ufp.esof.projecto.services.filters.Oferta.FilterOfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfertaService {

    private OfertaRepo ofertaRepo;
    private CadeiraRepo cadeiraRepo;
    private FilterOfertaService filterService;
    private ComponenteService componenteService;
    private ResultadoAprendizagemService resultadoAprendizagemService;

    @Autowired
    public OfertaService(OfertaRepo ofertaRepo, CadeiraRepo cadeiraRepo, FilterOfertaService filterService, ComponenteService componenteService, ResultadoAprendizagemService resultadoAprendizagemService) {
        this.ofertaRepo = ofertaRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.filterService = filterService;
        this.componenteService = componenteService;
        this.resultadoAprendizagemService = resultadoAprendizagemService;
    }

    public Set<Oferta> filterOferta(Map<String, String> searchParams){
        FilterOfertaObject filterOfertaObject= new FilterOfertaObject(searchParams);
        Set<Oferta> ofertas=this.findAll();
        return this.filterService.filter(ofertas, filterOfertaObject);
    }

    public Set<Oferta> findAll(String cadeira){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        Set<Oferta> ofertas=new HashSet<>();
        if (optionalCadeira.isPresent()){
            ofertas.addAll(optionalCadeira.get().getOfertas());
        }
        return ofertas;
    }

    public Set<Oferta> findAll(){
        Set<Oferta> ofertas=new HashSet<>();
        for (Oferta o : ofertaRepo.findAll()) {
            ofertas.add(o);
        }
        return ofertas;
    }

    public Optional<Oferta> find(String cadeira, int ano){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if(o.getAno() == ano){
                    return Optional.of(o);
                }
            }
        }
        return Optional.empty();
    }

    //verificar
    public Optional<Oferta> updateOferta(String cadeira, int ano, Oferta oferta){
        Optional<Oferta> optionalOferta = find(cadeira,ano);
        if (optionalOferta.isPresent()){
            ofertaRepo.save(oferta);
            return optionalOferta;
        }
        return Optional.empty();
    }

    public Optional<Oferta> createOferta(String cadeira, Oferta oferta){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno() == oferta.getAno()){
                    return Optional.empty();
                }
            }
            oferta.setCadeira(optionalCadeira.get());
            optionalCadeira.get().getOfertas().add(oferta);
            this.ofertaRepo.save(oferta);
            return Optional.of(oferta);
        }
        return Optional.empty();
    }

    public Boolean deleteOferta(String cadeira, int ano){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            for (Oferta o : optionalCadeira.get().getOfertas()) {
                if (o.getAno() == ano){
                    delete(o);
                    //optionalCadeira.get().getOfertas().remove(o);
                    //o.setCadeira(null);
                    //ofertaRepo.delete(o);
                    return true;
                }
            }
        }
        return false;
    }

    //acabar
    public void deleteAll(String cadeira){
        Optional<Cadeira> optionalCadeira = cadeiraRepo.findByDesignation(cadeira);
        if (optionalCadeira.isPresent()){
            while(!optionalCadeira.get().getOfertas().isEmpty()){
                Iterator<Oferta> ofertas = optionalCadeira.get().getOfertas().iterator();
                Oferta o = ofertas.next();
                //optionalCadeira.get().getOfertas().remove(o);
                //o.setCadeira(null);
                //ofertaRepo.delete(o);
                delete(o);
            }
        }
    }

    public void delete(Oferta o){
        o.getCadeira().getOfertas().remove(o);
        o.setCadeira(null);
        for (Componente c : o.getComponentes()) {
            componenteService.delete(c);
        }
        for (ResultadoAprendizagem ra : o.getRas()){
            resultadoAprendizagemService.delete(ra);
        }
        ofertaRepo.delete(o);
    }
}
