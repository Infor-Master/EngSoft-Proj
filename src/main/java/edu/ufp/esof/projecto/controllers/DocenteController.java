package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Docente;
import edu.ufp.esof.projecto.services.DocenteService;
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
@RequestMapping("/docente")
public class DocenteController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private DocenteService docenteService;

    @Autowired
    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Docente>> getAllDocentes(){
        this.logger.info("Listing Request for docentes");

        return ResponseEntity.ok(this.docenteService.findAll());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Docente> getDocenteById(@PathVariable("id") String id) throws DocenteController.NoDocenteException {
        this.logger.info("Listing Request for docente with id " + id);

        Optional<Docente> optionalDocente =this.docenteService.findByNumber(id);
        if(optionalDocente.isPresent()) {
            return ResponseEntity.ok(optionalDocente.get());
        }
        throw new DocenteController.NoDocenteException(id);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Docente>editDocente(@PathVariable("id") String id, @RequestBody Docente docente) throws DocenteController.NoDocenteException {
        this.logger.info("Update Request for docente with id " + id);

        Optional<Docente> optionalDocente =this.docenteService.updateDocente(id, docente);
        if(optionalDocente.isPresent()) {
            return ResponseEntity.ok(optionalDocente.get());
        }
        throw new DocenteController.NoDocenteException(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllDocentes(){
        this.logger.info("Delete Request for every docente");

        docenteService.deleteAll();
        return ResponseEntity.ok("Deleted every docente");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteDocente(@PathVariable("id") String id) throws DocenteController.NoDocenteException {
        this.logger.info("Delete Request for docente with id " + id);

        Boolean deleted = this.docenteService.deleteDocente(id);
        if(deleted) {
            return ResponseEntity.ok("Delete docente with id = " + id);
        }
        throw new DocenteController.NoDocenteException(id);
    }
    








    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Docente> createDocente(@RequestBody Docente docente){
        this.logger.info("Create Docente Request with name = " + docente.getName() + " and code = " + docente.getCode());

        Optional<Docente> docenteOptional=this.docenteService.createDocente(docente);
        if(docenteOptional.isPresent()){
            return ResponseEntity.ok(docenteOptional.get());
        }
        throw new DocenteController.DocenteAlreadyExistsException(docente.getName());
    }
    
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Docente não existente")
    private static class NoDocenteException extends RuntimeException {

        private NoDocenteException(String id) {
            super("Docente com id " + id + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Docente já existente")
    private static class DocenteAlreadyExistsException extends RuntimeException {

        public DocenteAlreadyExistsException(String name) {
            super("Docente com nome: " + name + " já existe");
        }
    }
}
