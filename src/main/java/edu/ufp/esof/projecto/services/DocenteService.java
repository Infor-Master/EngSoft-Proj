package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Docente;
import edu.ufp.esof.projecto.repositories.DocenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DocenteService {

    private DocenteRepo docenteRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public DocenteService(DocenteRepo docenteRepo) {
        this.docenteRepo = docenteRepo;
    }


    public Set<Docente> findAll() {
        Set<Docente> docentes=new HashSet<>();
        for(Docente d:this.docenteRepo.findAll()){
            docentes.add(d);
        }
        return docentes;
    }

    public Optional<Docente> findByNumber(String id) {
        Optional<Docente> optionalDocente = Optional.empty();
        for(Docente d:this.docenteRepo.findAll()){
            if (d.getCode().compareTo(id) == 0){
                optionalDocente = Optional.of(d);
                break;
            }
        }
        return optionalDocente;
    }

    public Optional<Docente> createDocente(Docente docente) {
        Optional<Docente> optionalDocente=this.docenteRepo.findByCode(docente.getCode());
        if(optionalDocente.isPresent()){
            return Optional.empty();
        }
        Docente createdDocente=this.docenteRepo.save(docente);
        return Optional.of(createdDocente);
    }

    public Optional<Docente> updateDocente(String code, Docente docente){
        Optional<Docente> optionalDocente=this.docenteRepo.findByCode(code);
        if(optionalDocente.isPresent()){
            docenteRepo.save(docente);
            return optionalDocente;
        }
        return Optional.empty();
    }

    public Boolean deleteDocente(String code){
        Optional<Docente> optionalDocente=this.docenteRepo.findByCode(code);
        if(optionalDocente.isPresent()){
            docenteRepo.delete(optionalDocente.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        docenteRepo.deleteAll();
    }
}
