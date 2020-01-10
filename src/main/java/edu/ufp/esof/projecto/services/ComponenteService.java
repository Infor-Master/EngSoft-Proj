package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.repositories.ComponenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ComponenteService {

    private ComponenteRepo componenteRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public ComponenteService(ComponenteRepo componenteRepo) {
        this.componenteRepo = componenteRepo;
    }

    public Set<Componente> findAll() {
        Set<Componente> componentes=new HashSet<>();
        for(Componente c:this.componenteRepo.findAll()){
            componentes.add(c);
        }
        return componentes;
    }

    public Optional<Componente> findByType(String type) {
        Optional<Componente> optionalComponente = Optional.empty();
        for(Componente c:this.componenteRepo.findAll()){
            if (c.getType().compareTo(type) == 0){
                optionalComponente = Optional.of(c);
                break;
            }
        }
        return optionalComponente;
    }

    public Optional<Componente> createComponente(Componente componente) {
        Optional<Componente> optionalComponente=this.componenteRepo.findByType(componente.getType());
        if(optionalComponente.isPresent()){
            return Optional.empty();
        }
        Componente createdComponente=this.componenteRepo.save(componente);
        return Optional.of(createdComponente);
    }

    public Optional<Componente> updateComponente(String type, Componente componente){
        Optional<Componente> optionalComponente=this.componenteRepo.findById(componente.getId());
        if(optionalComponente.isPresent()){
            componenteRepo.save(componente);
            return optionalComponente;
        }
        return Optional.empty();
    }

    public Boolean deleteComponente(String type){
        Optional<Componente> optionalComponente=this.componenteRepo.findByType(type);
        if(optionalComponente.isPresent()){
            componenteRepo.delete(optionalComponente.get());
            return true;
        }
        return false;
    }

    // está mal, tem de ser específico a disciplina
    public void deleteAll(){
        componenteRepo.deleteAll();
    }
}
