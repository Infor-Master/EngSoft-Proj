package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.OfertaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/oferta")
public class OfertaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private OfertaService ofertaService;

    @Autowired
    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Oferta>> searchCadeiras(@RequestParam Map<String,String> query){
        return ResponseEntity.ok(this.ofertaService.filterOferta(query));
    }

}
