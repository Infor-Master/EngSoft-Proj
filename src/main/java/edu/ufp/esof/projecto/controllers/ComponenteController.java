package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.services.ComponenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/componente")
public class ComponenteController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ComponenteService componenteService;

    @Autowired
    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Componente>> searchComponentes(@RequestParam Map<String,String> query){
        return ResponseEntity.ok(this.componenteService.filterComponente(query));
    }
}
