package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.CadeiraRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadeiraService {

    private CadeiraRepo cadeiraRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public CadeiraService(CadeiraRepo cadeiraRepo) {
        this.cadeiraRepo = cadeiraRepo;
    }
}
