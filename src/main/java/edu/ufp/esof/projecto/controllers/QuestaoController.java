package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.services.QuestaoService;
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
@RequestMapping("/questao")
public class QuestaoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private QuestaoService questaoService;

    @Autowired
    public QuestaoController(QuestaoService questaoService) {
        this.questaoService = questaoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Questao>> getAllQuestoes(){
        this.logger.info("Listing Request for Questoes");

        return ResponseEntity.ok(this.questaoService.findAll());
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.GET)
    public ResponseEntity<Questao> getQuestaoById(@PathVariable("designation") String designation) throws NoQuestaoException{
        this.logger.info("Listing Request for Questao with designation " + designation);

        Optional<Questao> optionalQuestao=this.questaoService.findByDesignation(designation);
        if(optionalQuestao.isPresent()){
            return ResponseEntity.ok(optionalQuestao.get());
        }
        throw new NoQuestaoException(designation);
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.PUT)
    public ResponseEntity<Questao>editQuestao(@PathVariable("designation") String designation, @RequestBody Questao questao) throws NoQuestaoException{
        this.logger.info("Update Request for questao with designation " + designation);

        Optional<Questao> optionalQuestao=this.questaoService.updateQuestao(designation,questao);
        if(optionalQuestao.isPresent()){
            return ResponseEntity.ok(optionalQuestao.get());
        }
        throw new NoQuestaoException(designation);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllQuestoes(){
        this.logger.info("Delete Request for every questao");

        questaoService.deleteAll();
        return ResponseEntity.ok("Deleted every questao");
    }

    @RequestMapping(value = "/{designation}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteQuestao(@PathVariable("designation") String designation) throws NoQuestaoException{
        this.logger.info("Delete Request for questao with designation " + designation);

        Boolean deleted = this.questaoService.deleteQuestao(designation);
        if(deleted){
            return ResponseEntity.ok("Delete questao with designation = " + designation);
        }
        throw new NoQuestaoException(designation);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Questao> createQuestao(@RequestBody Questao questao){
        this.logger.info("Create Questao Request with designation = " + questao.getDesignation());

        Optional<Questao> optionalQuestao=this.questaoService.createQuestao(questao);
        if(optionalQuestao.isPresent()){
            return ResponseEntity.ok(optionalQuestao.get());
        }
        throw new QuestaoAlreadyExistsException(questao.getDesignation());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Questao não existente")
    private static class NoQuestaoException extends RuntimeException {

        private NoQuestaoException(String designation) {
            super("Questao com designacao " + designation + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Questao já existente")
    private static class QuestaoAlreadyExistsException extends RuntimeException {

        public QuestaoAlreadyExistsException(String designation) {
            super("Questao com designacao: " + designation + " já existe");
        }
    }




}
