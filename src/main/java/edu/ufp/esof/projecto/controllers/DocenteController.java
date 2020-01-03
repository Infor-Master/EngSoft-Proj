package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.DocenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private DocenteService docenteService;

    @Autowired
    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }
}
