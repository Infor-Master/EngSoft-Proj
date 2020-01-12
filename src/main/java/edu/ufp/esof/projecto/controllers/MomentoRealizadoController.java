package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.services.MomentoRealizadoService;
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
@RequestMapping("/momentoRealizado")
public class MomentoRealizadoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MomentoRealizadoService momentoRealizadoService;

    @Autowired
    public MomentoRealizadoController(MomentoRealizadoService momentoRealizadoService) {
        this.momentoRealizadoService = momentoRealizadoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<MomentoRealizado>> getAllResultadosAprendizagem(){
        this.logger.info("Listing Request for Momentos Realizados");
        return ResponseEntity.ok(this.momentoRealizadoService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MomentoRealizado> getMomentoRealizadoById(@PathVariable("id") Long id) throws MomentoRealizadoController.NoMomentoRealizadoException {
        this.logger.info("Listing Request for Momento Realizado with id " + id);

        Optional<MomentoRealizado> optionalMR =this.momentoRealizadoService.findById(id);
        if(optionalMR.isPresent()){
            return ResponseEntity.ok(optionalMR.get());
        }
        throw new MomentoRealizadoController.NoMomentoRealizadoException(id);
    }





    // Ver se preciso
    /*@RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<MomentoRealizado>editMomentoRealizado(@PathVariable("id") Long id, @RequestBody MomentoRealizado momentoRealizado) throws MomentoRealizadoController.NoMomentoRealizadoException {
        this.logger.info("Update Request for Momento Realizado with id " + id);

        Optional<MomentoRealizado> optionalMR=this.momentoRealizadoService.updateMomentoRealizado(id, momentoRealizado);
        if(optionalMR.isPresent()) {
            return ResponseEntity.ok(optionalMR.get());
        }
        throw new MomentoRealizadoController.NoMomentoRealizadoException(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MomentoRealizado> createMomentoRealizado(@RequestBody MomentoRealizado momentoRealizado){
        this.logger.info("Create Momento Realizado Request with id = " + momentoRealizado.getId());

        Optional<MomentoRealizado> OptionalMR=this.momentoRealizadoService.createMomentoRealizado(momentoRealizado);
        if(OptionalMR.isPresent()){
            return ResponseEntity.ok(OptionalMR.get());
        }
        throw new MomentoRealizadoController.MomentoRealizadoAlreadyExistsExcpetion(momentoRealizado.getId());
    }*/
    
    
    
    


    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Momento Realizado não existente")
    private static class NoMomentoRealizadoException extends RuntimeException{
        private NoMomentoRealizadoException(Long id){
            super("Momento Realizado com id " + id + " não existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Momento Realizado já existente")
    private static class MomentoRealizadoAlreadyExistsExcpetion extends RuntimeException {

        public MomentoRealizadoAlreadyExistsExcpetion(Long id) {
            super("Momento Realizado com id: " + id + " já existe");
        }
    }
}
