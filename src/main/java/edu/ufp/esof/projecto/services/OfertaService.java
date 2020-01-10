package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.repositories.OfertaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OfertaService {

    private OfertaRepo ofertaRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public OfertaService(OfertaRepo ofertaRepo) {
        this.ofertaRepo = ofertaRepo;
    }

    public Set<Oferta> findAll(){
        Set<Oferta> ofertas=new HashSet<>();
        for (Oferta o:this.ofertaRepo.findAll()) {
            ofertas.add(o);
        }
        return ofertas;
    }

    public Optional<Oferta> findById(Long id){
        Optional<Oferta> optionalOferta=Optional.empty();
        for (Oferta o:this.ofertaRepo.findAll()) {
            if(o.getId().compareTo(id)==0){
                optionalOferta=Optional.of(o);
                break;
            }
        }
        return optionalOferta;
    }

    public Optional<Oferta> updateOferta(Long id, Oferta oferta){
        Optional<Oferta> optionalOferta=this.ofertaRepo.findById(id);
        if(optionalOferta.isPresent()){
            ofertaRepo.save(oferta);
            return optionalOferta;
        }
        return Optional.empty();
    }

    public Optional<Oferta> createOferta(Oferta oferta){
        Optional<Oferta> optionalOferta=this.ofertaRepo.findById(oferta.getId());
        if(optionalOferta.isPresent()){
            return Optional.empty();
        }
        Oferta createdOferta=this.ofertaRepo.save(oferta);
        return Optional.of(createdOferta);
    }

    public Boolean deleteOferta(Long id){
        Optional<Oferta> optionalOferta=this.ofertaRepo.findById(id);
        if(optionalOferta.isPresent()){
            ofertaRepo.delete(optionalOferta.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        ofertaRepo.deleteAll();
    }
}
