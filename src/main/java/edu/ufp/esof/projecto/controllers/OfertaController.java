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

    /*@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Oferta>> getAllOfertas(){
        this.logger.info("Listing Request for ofertas");
        return ResponseEntity.ok(this.ofertaService.findAll());
    }*/

    /*@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Oferta> getOfertaById(@PathVariable("id") Long id) throws NoOfertaException{
        this.logger.info("Listing Request for oferta with id " + id);
        Optional<Oferta> optionalOferta=this.ofertaService.findById(id);
        if(optionalOferta.isPresent()){
            return ResponseEntity.ok(optionalOferta.get());
        }
        throw new NoOfertaException(id);
    }*/

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Oferta>> searchCadeiras(@RequestParam Map<String,String> query){
        return ResponseEntity.ok(this.ofertaService.filterOferta(query));
    }

    /*@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Oferta>editOferta(@PathVariable("id") Long id, @RequestBody Oferta oferta) throws NoOfertaException{
        this.logger.info("Update Request for oferta with id " + id);

        Optional<Oferta> optionalOferta=this.ofertaService.updateOferta(id, oferta);
        if(optionalOferta.isPresent()){
            return ResponseEntity.ok(optionalOferta.get());
        }
        throw new NoOfertaException(id);
    }*/

    /*@RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllOfertas(){
        this.logger.info("Delete Request for every oferta");

        ofertaService.deleteAll();
        return ResponseEntity.ok("Deleted every oferta");
    }*/

    /*@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteOferta(@PathVariable("id") Long id) throws NoOfertaException{
        this.logger.info("Delete Request for oferta with id " + id);
        Boolean deleted = this.ofertaService.deleteOferta(id);
        if(deleted){
            return ResponseEntity.ok("Delete oferta with id = " + id);
        }
        throw new NoOfertaException(id);
    }*/

    /*@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Oferta> createOferta(@RequestBody Oferta oferta){
        this.logger.info("Create Oferta Request with ano = " + oferta.getAno() + " and cadeira code = " + oferta.getCadeira().getCode());

        Optional<Oferta> optionalOferta=this.ofertaService.createOferta(oferta);
        if(optionalOferta.isPresent()){
            return ResponseEntity.ok(optionalOferta.get());
        }
        throw new OfertaAlreadyExistsException(oferta.getId());
    }*/


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Oferta não existente")
    private static class NoOfertaException extends RuntimeException{
        private NoOfertaException(Long id){super("Oferta com o id " + id + "nao existente");}
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Oferta já existente")
    private static class OfertaAlreadyExistsException extends RuntimeException{
        public OfertaAlreadyExistsException(Long id){super("Oferta com o id : " + id + " já existe");}
    }
}
