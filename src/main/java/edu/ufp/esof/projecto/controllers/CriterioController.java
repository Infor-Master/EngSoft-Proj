package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.services.CriterioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/criterio")
public class CriterioController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CriterioService criterioService;

    @Autowired
    public CriterioController(CriterioService criterioService) {
        this.criterioService = criterioService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Criterio>> getAllCriterios(){
        this.logger.info("Listing Request for criterios");

        return ResponseEntity.ok(this.criterioService.findAll());
    }

    @RequestMapping(value = "/{designation}",method = RequestMethod.GET)
    public ResponseEntity<Criterio> getCriterio(@PathVariable("designation") String designation) throws NoCriterioExcpetion {
        this.logger.info("Listing Request for criterio " + designation);

        Optional<Criterio> optionalCriterio =this.criterioService.findByDesignation(designation);
        if(optionalCriterio.isPresent()) {
            return ResponseEntity.ok(optionalCriterio.get());
        }
        throw new NoCriterioExcpetion(designation);
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.PUT)
    public ResponseEntity<Criterio>editCriterio(@PathVariable("designation") String designation, @RequestBody Criterio criterio) throws NoCriterioExcpetion{
        this.logger.info("Update Request for criterio " + designation);

        Optional<Criterio> optionalCriterio =this.criterioService.updateCriterio(designation, criterio);
        if(optionalCriterio.isPresent()) {
            return ResponseEntity.ok(optionalCriterio.get());
        }
        throw new NoCriterioExcpetion(designation);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllCriterios(){
        this.logger.info("Delete Request for every criterio");

        criterioService.deleteAll();
        return ResponseEntity.ok("Deleted every criterio");
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCriterio(@PathVariable("designation") String designation) throws NoCriterioExcpetion{
        this.logger.info("Delete Request for criterio " + designation);

        Boolean deleted = this.criterioService.deleteCriterio(designation);
        if(deleted) {
            return ResponseEntity.ok("Delete criterio " + designation);
        }
        throw new NoCriterioExcpetion(designation);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Criterio> createCriterio(@RequestBody Criterio criterio){
        this.logger.info("Create Criterio Request");

        Optional<Criterio> criterioOptional=this.criterioService.createCriterio(criterio);
        if(criterioOptional.isPresent()){
            return ResponseEntity.ok(criterioOptional.get());
        }
        throw new CriterioAlreadyExistsExcpetion(criterio.getDesignation());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Criterio não existente")
    private static class NoCriterioExcpetion extends RuntimeException {

        private NoCriterioExcpetion(String designation) {
            super("Criterio " + designation + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Criterio já existente")
    private static class CriterioAlreadyExistsExcpetion extends RuntimeException {

        public CriterioAlreadyExistsExcpetion(String designation) {
            super("Criterio " + designation + " já existe");
        }
    }
}
