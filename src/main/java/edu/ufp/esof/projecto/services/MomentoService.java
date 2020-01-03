package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.MomentoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MomentoService {

    private MomentoRepo momentoRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public MomentoService(MomentoRepo momentoRepo) {
        this.momentoRepo = momentoRepo;
    }
}
