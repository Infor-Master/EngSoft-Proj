package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.ResultadoAprendizagem;
import edu.ufp.esof.projecto.services.ResultadoAprendizagemService;
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
@RequestMapping("/resultadoAprendizagem")
public class ResultadoAprendizagemController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ResultadoAprendizagemService resultadoAprendizagemService;

    @Autowired
    public ResultadoAprendizagemController(ResultadoAprendizagemService resultadoAprendizagemService) {
        this.resultadoAprendizagemService = resultadoAprendizagemService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<ResultadoAprendizagem>> getAllResultadosAprendizagem(){
        this.logger.info("Listing Request for Resultados Aprendizagem");
        return ResponseEntity.ok(this.resultadoAprendizagemService.findAll());
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.GET)
    public ResponseEntity<ResultadoAprendizagem> getResultadoAprendizagemById(@PathVariable("designation") String designation) throws NoResultadoAprendizagemException{
        this.logger.info("Listing Request for Resultado Aprendizagem with designation " + designation);

        Optional<ResultadoAprendizagem> optionalRA =this.resultadoAprendizagemService.findByDesignation(designation);
        if(optionalRA.isPresent()){
            return ResponseEntity.ok(optionalRA.get());
        }
        throw new NoResultadoAprendizagemException(designation);
    }

    @RequestMapping(value = "{designation}", method = RequestMethod.PUT)
    public ResponseEntity<ResultadoAprendizagem>editResultadoAprendizagem(@PathVariable("designation") String designation, @RequestBody ResultadoAprendizagem resultadoAprendizagem) throws NoResultadoAprendizagemException {
        this.logger.info("Update Request for Resultado Aprendizagem with designation " + designation);

        Optional<ResultadoAprendizagem> optionalRA=this.resultadoAprendizagemService.updateResultadoAprendizagem(designation, resultadoAprendizagem);
        if(optionalRA.isPresent()) {
            return ResponseEntity.ok(optionalRA.get());
        }
        throw new NoResultadoAprendizagemException(designation);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllResultadosAprendizagem(){
        this.logger.info("Delete Request for every Resultado Aprendizagem");

        resultadoAprendizagemService.deleteAll();
        return ResponseEntity.ok("Deleted every Resultado Aprendizagem");
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteResultadoAprendizagem(@PathVariable("designation") String designation) throws NoResultadoAprendizagemException{
        this.logger.info("Delete Request for Resultado Aprendizagem with designation " + designation);


        Boolean deleted = this.resultadoAprendizagemService.deleteResultadoAprendizagem(designation);
        if(deleted){
            return ResponseEntity.ok("Delete Resultado Aprendizagem with designation = " + designation);
        }
        throw new NoResultadoAprendizagemException(designation);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultadoAprendizagem> createResultadoAprendizagem(@RequestBody ResultadoAprendizagem resultadoAprendizagem){
        this.logger.info("Create Resultado Aprendizagem Request with designation = " + resultadoAprendizagem.getDesignation());

        Optional<ResultadoAprendizagem> OptionalRA=this.resultadoAprendizagemService.createResultadoAprendizagem(resultadoAprendizagem);
        if(OptionalRA.isPresent()){
            return ResponseEntity.ok(OptionalRA.get());
        }
        throw new ResultadoAprendizagemAlreadyExistsExcpetion(resultadoAprendizagem.getDesignation());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Resultado Aprendizagem não existente")
    private static class NoResultadoAprendizagemException extends RuntimeException{
        private NoResultadoAprendizagemException(String id){
            super("Resultado Aprendizagem com id " + id + " não existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Resultado Aprendizagem já existente")
    private static class ResultadoAprendizagemAlreadyExistsExcpetion extends RuntimeException {

        public ResultadoAprendizagemAlreadyExistsExcpetion(String designation) {
            super("Resultado Aprendizagem com designação: " + designation + " já existe");
        }
    }
}
