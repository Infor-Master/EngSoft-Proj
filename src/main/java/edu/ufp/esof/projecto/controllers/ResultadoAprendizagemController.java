package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.ResultadoAprendizagemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resultadoAprendizagem")
public class ResultadoAprendizagemController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ResultadoAprendizagemService resultadoAprendizagemService;

    @Autowired
    public ResultadoAprendizagemController(ResultadoAprendizagemService resultadoAprendizagemService) {
        this.resultadoAprendizagemService = resultadoAprendizagemService;
    }
}
