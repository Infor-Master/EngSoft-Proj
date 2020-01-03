package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.CriterioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class CriterioService {

    private CriterioRepo criterioRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public CriterioService(CriterioRepo criterioRepo) {
        this.criterioRepo = criterioRepo;
    }
}
