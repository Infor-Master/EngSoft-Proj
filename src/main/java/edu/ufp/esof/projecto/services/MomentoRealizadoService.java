package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.MomentoRealizadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MomentoRealizadoService {

    private MomentoRealizadoRepo momentoRealizadoRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public MomentoRealizadoService(MomentoRealizadoRepo momentoRealizadoRepo) {
        this.momentoRealizadoRepo = momentoRealizadoRepo;
    }
}
