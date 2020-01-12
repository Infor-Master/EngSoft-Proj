package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.services.MomentoService;
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
@RequestMapping("/momento")
public class MomentoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MomentoService momentoService;

    @Autowired
    public MomentoController(MomentoService momentoService) {
        this.momentoService = momentoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Momento>> getAllMomentos(){
        this.logger.info("Listing Request for Momentos");
        return ResponseEntity.ok(this.momentoService.findAll());
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.GET)
    public ResponseEntity<Momento> getMomentoById(@PathVariable("designation") String designation) throws MomentoController.NoMomentoException {
        this.logger.info("Listing Request for Momento with designation " + designation);

        Optional<Momento> optionalRA =this.momentoService.findByDesignation(designation);
        if(optionalRA.isPresent()){
            return ResponseEntity.ok(optionalRA.get());
        }
        throw new MomentoController.NoMomentoException(designation);
    }

    /*@RequestMapping(value = "{designation}", method = RequestMethod.PUT)
    public ResponseEntity<Momento>editMomento(@PathVariable("designation") String designation, @RequestBody Momento momento) throws MomentoController.NoMomentoException {
        this.logger.info("Update Request for Momento with designation " + designation);

        Optional<Momento> optionalRA=this.momentoService.updateMomento(designation, momento);
        if(optionalRA.isPresent()) {
            return ResponseEntity.ok(optionalRA.get());
        }
        throw new MomentoController.NoMomentoException(designation);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllMomentos(){
        this.logger.info("Delete Request for every Momento");

        momentoService.deleteAll();
        return ResponseEntity.ok("Deleted every Momento");
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMomento(@PathVariable("designation") String designation) throws MomentoController.NoMomentoException {
        this.logger.info("Delete Request for Momento with designation " + designation);


        Boolean deleted = this.momentoService.deleteMomento(designation);
        if(deleted){
            return ResponseEntity.ok("Delete Momento with designation = " + designation);
        }
        throw new MomentoController.NoMomentoException(designation);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Momento> createMomento(@RequestBody Momento momento){
        this.logger.info("Create Momento Request with designation = " + momento.getDesignation());

        Optional<Momento> OptionalRA=this.momentoService.createMomento(momento);
        if(OptionalRA.isPresent()){
            return ResponseEntity.ok(OptionalRA.get());
        }
        throw new MomentoController.MomentoAlreadyExistsExcpetion(momento.getDesignation());
    }*/

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Momento não existente")
    private static class NoMomentoException extends RuntimeException{
        private NoMomentoException(String id){
            super("Momento com id " + id + " não existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Momento já existente")
    private static class MomentoAlreadyExistsExcpetion extends RuntimeException {

        public MomentoAlreadyExistsExcpetion(String designation) {
            super("Momento com designação: " + designation + " já existe");
        }
    }
    
    
}
