package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.ComponenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponenteService {

    private ComponenteRepo componenteRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public ComponenteService(ComponenteRepo componenteRepo) {
        this.componenteRepo = componenteRepo;
    }
}
