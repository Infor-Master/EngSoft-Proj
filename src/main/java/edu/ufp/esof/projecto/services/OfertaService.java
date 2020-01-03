package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.OfertaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfertaService {

    private OfertaRepo ofertaRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public OfertaService(OfertaRepo ofertaRepo) {
        this.ofertaRepo = ofertaRepo;
    }
}
