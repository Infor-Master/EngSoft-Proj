package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.ComponenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/componente")
public class ComponenteController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ComponenteService componenteService;

    @Autowired
    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }
}
