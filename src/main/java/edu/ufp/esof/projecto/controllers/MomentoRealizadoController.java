package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.MomentoRealizadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/momentoRealizado")
public class MomentoRealizadoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MomentoRealizadoService momentoRealizadoService;

    @Autowired
    public MomentoRealizadoController(MomentoRealizadoService momentoRealizadoService) {
        this.momentoRealizadoService = momentoRealizadoService;
    }
}
