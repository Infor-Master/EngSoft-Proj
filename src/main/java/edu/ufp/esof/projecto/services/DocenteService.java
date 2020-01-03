package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.DocenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class DocenteService {

    private DocenteRepo docenteRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public DocenteService(DocenteRepo docenteRepo) {
        this.docenteRepo = docenteRepo;
    }
}
