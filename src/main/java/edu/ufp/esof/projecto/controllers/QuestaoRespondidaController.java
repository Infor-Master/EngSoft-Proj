package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.QuestaoRespondida;
import edu.ufp.esof.projecto.services.QuestaoRespondidaService;
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
@RequestMapping("/questaoRespondida")
public class QuestaoRespondidaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private QuestaoRespondidaService questaoRespondidaService;

    @Autowired
    public QuestaoRespondidaController(QuestaoRespondidaService questaoRespondidaService) {
        this.questaoRespondidaService = questaoRespondidaService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<QuestaoRespondida>> getAllQuestaoRespondidas(){
        this.logger.info("Listing Request for questoes Respondidas");
        return ResponseEntity.ok(this.questaoRespondidaService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<QuestaoRespondida> getQuestaoRespondidaById(@PathVariable("id") Long id) throws QuestaoRespondidaController.NoQuestaoRespondidaException {
        this.logger.info("Listing Request for questao Respondida with id " + id);
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaService.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            return ResponseEntity.ok(optionalQuestaoRespondida.get());
        }
        throw new QuestaoRespondidaController.NoQuestaoRespondidaException(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<QuestaoRespondida>editQuestaoRespondida(@PathVariable("id") Long id, @RequestBody QuestaoRespondida questaoRespondida) throws QuestaoRespondidaController.NoQuestaoRespondidaException {
        this.logger.info("Update Request for questao Respondida with id " + id);

        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaService.updateQuestaoRespondida(id, questaoRespondida);
        if(optionalQuestaoRespondida.isPresent()){
            return ResponseEntity.ok(optionalQuestaoRespondida.get());
        }
        throw new QuestaoRespondidaController.NoQuestaoRespondidaException(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestaoRespondida> createQuestaoRespondida(@RequestBody QuestaoRespondida questaoRespondida){
        this.logger.info("Create QuestaoRespondida Request with id = " + questaoRespondida.getId());

        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaService.createQuestaoRespondida(questaoRespondida);
        if(optionalQuestaoRespondida.isPresent()){
            return ResponseEntity.ok(optionalQuestaoRespondida.get());
        }
        throw new QuestaoRespondidaController.QuestaoRespondidaAlreadyExistsException(questaoRespondida.getId());
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Questao Respondida não existente")
    private static class NoQuestaoRespondidaException extends RuntimeException{
        private NoQuestaoRespondidaException(Long id){super("Questao Respondida com o id " + id + "nao existente");}
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Questao Respondida já existente")
    private static class QuestaoRespondidaAlreadyExistsException extends RuntimeException{
        public QuestaoRespondidaAlreadyExistsException(Long id){super("Questao Respondida com o id : " + id + " já existe");}
    }
}
