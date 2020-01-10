package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.services.CadeiraService;
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
@RequestMapping("/cadeira")
public class CadeiraController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CadeiraService cadeiraService;

    @Autowired
    public CadeiraController(CadeiraService cadeiraService) {
        this.cadeiraService = cadeiraService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Cadeira>> getAllAlunos(){
        this.logger.info("Listing Request for cadeiras");

        return ResponseEntity.ok(this.cadeiraService.findAll());
    }

    /*@RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Cadeira> getCadeiraById(@PathVariable("id") String id) throws NoCadeiraException {
        this.logger.info("Listing Request for cadeira with id " + id);

        Optional<Cadeira> optionalCadeira =this.cadeiraService.findByNumber(id);
        if(optionalCadeira.isPresent()) {
            return ResponseEntity.ok(optionalCadeira.get());
        }
        throw new NoCadeiraException(id);
    }*/

    @RequestMapping(value = "/{nome}",method = RequestMethod.GET)
    public ResponseEntity<Cadeira> getCadeiraByName(@PathVariable("nome") String nome) throws NoCadeiraException {
        this.logger.info("Listing Request for cadeira named " + nome);

        Optional<Cadeira> optionalCadeira =this.cadeiraService.findByName(nome);
        if(optionalCadeira.isPresent()) {
            return ResponseEntity.ok(optionalCadeira.get());
        }
        throw new NoNamedCadeiraException(nome);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllCadeiras(){
        this.logger.info("Delete Request for every cadeira");

        cadeiraService.deleteAll();
        return ResponseEntity.ok("Deleted every cadeira");
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAluno(@PathVariable("code") String code) throws NoNamedCadeiraException{
        this.logger.info("Delete Request for cadeira with id " + code);

        Boolean deleted = this.cadeiraService.deleteCadeira(code);
        if(deleted) {
            return ResponseEntity.ok("Delete cadeira named = " + code);//new ResponseEntity<>("Delete aluno with id = " + id, HttpStatus.OK);
        }
        throw new NoCadeiraException(code);
    }


    @RequestMapping(value = "/{nome}", method = RequestMethod.PUT)
    public ResponseEntity<Cadeira>editCadeira(@PathVariable("nome") String nome, @RequestBody Cadeira cadeira) throws NoNamedCadeiraException{
        this.logger.info("Update Request for aluno named " + nome);

        Optional<Cadeira> optionalCadeira =this.cadeiraService.updateCadeira(nome, cadeira);
        if(optionalCadeira.isPresent()) {
            return ResponseEntity.ok(optionalCadeira.get());
        }
        throw new NoNamedCadeiraException(nome);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cadeira> createCadeira(@RequestBody Cadeira cadeira){
        this.logger.info("Create Cadeira Request with name = " + cadeira.getDesignation() + " and code = " + cadeira.getCode());

        Optional<Cadeira> cadeiraOptional=this.cadeiraService.createCadeira(cadeira);
        if(cadeiraOptional.isPresent()){
            return ResponseEntity.ok(cadeiraOptional.get());
        }
        throw new CadeiraAlreadyExistsExcpetion(cadeira.getDesignation());
    }




    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Cadeira não existente")
    private static class NoCadeiraException extends RuntimeException {

        private NoCadeiraException(String id) {
            super("Cadeira com id " + id + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Cadeira não existente")
    private static class NoNamedCadeiraException extends RuntimeException {

        private NoNamedCadeiraException(String name) {
            super("Cadeira " + name + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Cadeira já existente")
    private static class CadeiraAlreadyExistsExcpetion extends RuntimeException {

        public CadeiraAlreadyExistsExcpetion(String name) {
            super("Cadeira com nome: " + name + " já existe");
        }
    }
}
