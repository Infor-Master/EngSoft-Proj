package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.OfertaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oferta")
public class OfertaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private OfertaService ofertaService;

    @Autowired
    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }
}
