package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.services.ComponenteService;
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

    /*@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Componente>> getAllComponentes(){
        this.logger.info("Listing Request for Componentes");

        return ResponseEntity.ok(this.componenteService.findAll());
    }*/

    /*@RequestMapping(value = "/{type}",method = RequestMethod.GET)
    public ResponseEntity<Componente> getComponente(@PathVariable("type") String type) throws NoComponenteException {
        this.logger.info("Listing Request for component with type = " + type);

        Optional<Componente> optionalComponente =this.componenteService.findByType(type);
        if(optionalComponente.isPresent()) {
            return ResponseEntity.ok(optionalComponente.get());
        }
        throw new NoComponenteException(type);
    }*/

    /*@RequestMapping(value = "/{type}", method = RequestMethod.PUT)
    public ResponseEntity<Componente>editComponente(@PathVariable("type") String type, @RequestBody Componente componente) throws NoComponenteException{
        this.logger.info("Update Request for componente " + type);

        Optional<Componente> optionalComponente =this.componenteService.updateComponente(type, componente);
        if(optionalComponente.isPresent()) {
            return ResponseEntity.ok(optionalComponente.get());
        }
        throw new NoComponenteException(type);
    }*/

    /*@RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllComponentes(){
        this.logger.info("Delete Request for every componente");

        componenteService.deleteAll();
        return ResponseEntity.ok("Deleted every componente");
    }*/

    /*@RequestMapping(value = "/{type}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteComponente(@PathVariable("type") String type) throws NoComponenteException{
        this.logger.info("Delete Request for componente " + type);

        Boolean deleted = this.componenteService.deleteComponente(type);
        if(deleted) {
            return ResponseEntity.ok("Delete componente " + type);
        }
        throw new NoComponenteException(type);
    }*/

    /*@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Componente> createComponente(@RequestBody Componente componente){
        this.logger.info("Create Componente Request");

        Optional<Componente> componenteOptional=this.componenteService.createComponente(componente);
        if(componenteOptional.isPresent()){
            return ResponseEntity.ok(componenteOptional.get());
        }
        throw new ComponenteAlreadyExistsExcpetion(componente.getType());
    }*/

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Componente não existente")
    private static class NoComponenteException extends RuntimeException {

        private NoComponenteException(String type) {
            super("Componente " + type + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Componente já existente")
    private static class ComponenteAlreadyExistsExcpetion extends RuntimeException {

        public ComponenteAlreadyExistsExcpetion(String type) {
            super("Componente " + type + " já existe");
        }
    }
}
