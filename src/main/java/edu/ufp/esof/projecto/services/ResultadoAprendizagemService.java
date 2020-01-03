package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.repositories.ResultadoAprendizagemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultadoAprendizagemService {

    private ResultadoAprendizagemRepo resultadoAprendizagemRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public ResultadoAprendizagemService(ResultadoAprendizagemRepo resultadoAprendizagemRepo) {
        this.resultadoAprendizagemRepo = resultadoAprendizagemRepo;
    }
}
