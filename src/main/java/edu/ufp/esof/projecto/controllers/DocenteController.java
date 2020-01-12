package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Docente;
import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.services.DocenteService;
import edu.ufp.esof.projecto.services.MomentoService;
import edu.ufp.esof.projecto.services.QuestaoService;
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
@RequestMapping("/docente")
public class DocenteController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private DocenteService docenteService;
    private MomentoService momentoService;
    private QuestaoService questaoService;

    @Autowired
    public DocenteController(DocenteService docenteService, MomentoService momentoService, QuestaoService questaoService) {
        this.docenteService = docenteService;
        this.momentoService = momentoService;
        this.questaoService = questaoService;
    }


    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Docente>> searchDocentes(@RequestParam Map<String,String> query){
        return ResponseEntity.ok(this.docenteService.filterDocente(query));
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
        throw new NoDocenteException(id);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Docente>editDocente(@PathVariable("id") String id, @RequestBody Docente docente) throws DocenteController.NoDocenteException {
        this.logger.info("Update Request for docente with id " + id);

        Optional<Docente> optionalDocente =this.docenteService.updateDocente(id, docente);
        if(optionalDocente.isPresent()) {
            return ResponseEntity.ok(optionalDocente.get());
        }
        throw new NoDocenteException(id);
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




    //-------------------------------------------//
    //              CADEIRA RELATED              //
    //-------------------------------------------//




    // ASSOCIAÇOES E DESASSOCIAÇOES NAO FUNCIONAM
    //@PostMapping(value = "/{id}/{cadeira}/{ano}/{comp]", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/{id}/{cadeira}/{ano}/{comp]", method = RequestMethod.PUT)
    public ResponseEntity<String>associateDocenteComponente(@PathVariable("id") String id, @PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("comp") String comp) throws NoDocenteException {
        this.logger.info("Associate Request for docente with id " + id + " to componente " + comp + " of " + cadeira + " in " + ano);

        Boolean associated =this.docenteService.associateDocenteComponente(id, cadeira,ano,comp);
        if(associated) {
            return ResponseEntity.ok("Docente " + id + " e cadeira " + cadeira + " associados");
        }
        throw new NoDocenteException(id);
    }

    @RequestMapping(value = "/{id}/{cadeira}/{ano}/{comp]", method = RequestMethod.DELETE)
    public ResponseEntity<String>desassociateDocenteComponente(@PathVariable("id") String id, @PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("comp") String comp) throws NoDocenteException {
        this.logger.info("Desassociate Request for docente with id " + id + " to componente " + comp + " of " + cadeira + " in " + ano);

        Boolean desassociated =this.docenteService.desassociateDocenteComponente(id, cadeira,ano,comp);
        if(desassociated) {
            return ResponseEntity.ok("Docente " + id + " e cadeira " + cadeira + " desassociados");
        }
        throw new NoDocenteException(id);
    }




    //-------------------------------------------//
    //              MOMENTO RELATED              //
    //-------------------------------------------//




    @PostMapping(value = "/{id}/{cadeira}/{ano}/{comp]/momento", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Momento> createMomento(@PathVariable("id") String id, @PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("comp") String comp, @RequestBody Momento momento){
        this.logger.info("Create Momento Request");

        Optional<Momento> momentoOptional=this.momentoService.createMomento(id,cadeira,ano,comp,momento);
        if(momentoOptional.isPresent()){
            return ResponseEntity.ok(momentoOptional.get());
        }
        throw new MomentoAlreadyExistsException(momento.getDesignation());
    }


    // Falta verificação se professor dá a cadeira
    @RequestMapping(value = "/{id}/{cadeira}/{ano}/{comp]/momento/{nome}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMomento(@PathVariable("id") String id, @PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("comp") String comp, @PathVariable("nome") String nome) throws NoMomentoException {
        this.logger.info("Delete Request for docente with id " + id);

        Boolean deleted = this.momentoService.deleteMomento(cadeira,ano,comp,nome);
        if(deleted) {
            return ResponseEntity.ok("Deleted momento " + nome);
        }
        throw new DocenteController.NoDocenteException(id);
    }




    //-------------------------------------------//
    //              QUESTAO RELATED              //
    //-------------------------------------------//




    // Falta verificar se professor da a cadeira
    @PostMapping(value = "/{id}/{cadeira}/{ano}/{comp]/{momento}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Questao> createQuest(@PathVariable("id") String id, @PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("comp") String comp, @PathVariable("momento") String momento, @RequestBody Questao questao){
        this.logger.info("Create Questao Request");

        Optional<Questao> questaoOptional = this.questaoService.createQuestao(id,cadeira,ano,comp,momento,questao);
        if(questaoOptional.isPresent()){
            return ResponseEntity.ok(questaoOptional.get());
        }
        throw new QuestaoAlreadyExistsException(questao.getDesignation());
    }

    // Falta verificação se professor dá a cadeira
    @RequestMapping(value = "/{id}/{cadeira}/{ano}/{comp]/{momento}/{nome}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMomento(@PathVariable("id") String id, @PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("comp") String comp, @PathVariable("momento") String momento, @PathVariable("nome") String nome) throws NoMomentoException {
        this.logger.info("Delete Request for questao");

        Boolean deleted = this.questaoService.deleteQuestao(id,cadeira,ano,comp,momento,nome);
        if(deleted) {
            return ResponseEntity.ok("Deleted momento " + nome);
        }
        throw new NoQuestaoException(nome);
    }




    //-------------------------------------------//
    //                 EXCEPTIONS                //
    //-------------------------------------------//




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

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Momento já existente")
    private static class MomentoAlreadyExistsException extends RuntimeException {

        public MomentoAlreadyExistsException(String name) {
            super("Momento com nome: " + name + " já existe");
        }
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Momento não existente")
    private static class NoMomentoException extends RuntimeException{
        private NoMomentoException(String nome){
            super("Momento " + nome + " não existente");
        }
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
