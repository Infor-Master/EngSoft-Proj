package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.CriterioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/criterio")
public class CriterioController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CriterioService criterioService;

    @Autowired
    public CriterioController(CriterioService criterioService) {
        this.criterioService = criterioService;
    }
}
