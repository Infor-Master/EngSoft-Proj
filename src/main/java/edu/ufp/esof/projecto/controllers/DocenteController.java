package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private DocenteService docenteService;
    private MomentoService momentoService;
    private QuestaoService questaoService;
    private EscalaService escalaService;
    private ResultadoAprendizagemService raService;
    private ComponenteService componenteService;
    private QuestaoRespondidaService questaoRespondidaService;

    @Autowired
    public DocenteController(DocenteService docenteService, QuestaoRespondidaService questaoRespondidaService, MomentoService momentoService, QuestaoService questaoService, EscalaService escalaService, ResultadoAprendizagemService raService, ComponenteService componenteService) {
        this.docenteService = docenteService;
        this.questaoRespondidaService = questaoRespondidaService;
        this.momentoService = momentoService;
        this.questaoService = questaoService;
        this.escalaService = escalaService;
        this.raService = raService;
        this.componenteService = componenteService;
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




    @PostMapping(value = "/momento", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Momento> createMomento(@RequestBody MomentoRequest momentoRequest){
        this.logger.info("Create Momento Request");

        Optional<Momento> momentoOptional=this.momentoService.createMomento(momentoRequest.getDocenteNumero(),momentoRequest.getCadeiraNome(),momentoRequest.getAno(),momentoRequest.getComp(), momentoRequest.getMomento());
        if(momentoOptional.isPresent()){
            return ResponseEntity.ok(momentoOptional.get());
        }
        throw new MomentoAlreadyExistsException(momentoRequest.getMomento().getDesignation());
    }

    @RequestMapping(value = "/momento", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMomento(@RequestBody MomentoRequest momentoRequest) throws NoMomentoException {
        this.logger.info("Delete Momento Request");

        Boolean deleted = this.momentoService.deleteMomento(momentoRequest);
        if(deleted) {
            return ResponseEntity.ok("Deleted momento " + momentoRequest.getMomento().getDesignation());
        }
        throw new DocenteController.NoDocenteException(momentoRequest.getDocenteNumero());
    }

    @RequestMapping(value = "/momento", method = RequestMethod.PUT)
    public ResponseEntity<Momento> updateMomento(@RequestBody MomentoRequest momentoRequest){
        this.logger.info("Update Request for momento");

        Optional<Momento> optionalMomento =this.momentoService.updateMomento(momentoRequest);
        if(optionalMomento.isPresent()) {
            return ResponseEntity.ok(optionalMomento.get());
        }
        throw new NoDocenteException(momentoRequest.getDocenteNumero());
    }

    //-------------------------------------------//
    //                RA RELATED                 //
    //-------------------------------------------//




    @PostMapping(value = "/ra", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultadoAprendizagem> createResultadoAprendizagem(@RequestBody RARequest resultadoAprendizagemRequest){
        this.logger.info("Create ResultadoAprendizagem Request");

        Optional<ResultadoAprendizagem> resultadoAprendizagemOptional=this.raService.createResultadoAprendizagem(resultadoAprendizagemRequest.getCadeiraNome(),resultadoAprendizagemRequest.getAno(),resultadoAprendizagemRequest.getResultadoAprendizagem());
        if(resultadoAprendizagemOptional.isPresent()){
            return ResponseEntity.ok(resultadoAprendizagemOptional.get());
        }
        throw new ResultadoAprendizagemAlreadyExistsException(resultadoAprendizagemRequest.getResultadoAprendizagem().getDesignation());
    }

    @RequestMapping(value = "/ra", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteResultadoAprendizagem(@RequestBody RARequest resultadoAprendizagemRequest) throws NoResultadoAprendizagemException {
        this.logger.info("Delete ResultadoAprendizagem Request");

        Boolean deleted = this.raService.deleteResultadoAprendizagem(resultadoAprendizagemRequest);
        if(deleted) {
            return ResponseEntity.ok("Deleted resultadoAprendizagem " + resultadoAprendizagemRequest.getResultadoAprendizagem().getDesignation());
        }
        throw new DocenteController.NoResultadoAprendizagemException(resultadoAprendizagemRequest.getResultadoAprendizagem().getDesignation());
    }

    @RequestMapping(value = "/ra", method = RequestMethod.PUT)
    public ResponseEntity<ResultadoAprendizagem> updateResultadoAprendizagem(@RequestBody RARequest resultadoAprendizagemRequest){
        this.logger.info("Update Request for resultadoAprendizagem");

        Optional<ResultadoAprendizagem> optionalResultadoAprendizagem =this.raService.updateResultadoAprendizagem(resultadoAprendizagemRequest);
        if(optionalResultadoAprendizagem.isPresent()) {
            return ResponseEntity.ok(optionalResultadoAprendizagem.get());
        }
        throw new NoResultadoAprendizagemException(resultadoAprendizagemRequest.getResultadoAprendizagem().getDesignation());
    }




    //-------------------------------------------//
    //              ESCALA RELATED              //
    //-------------------------------------------//




    @PostMapping(value = "/escala", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Escala> createEscala(@RequestBody EscalaRequest escalaRequest){
        this.logger.info("Create Escala Request");

        Optional<Escala> escalaOptional=this.escalaService.createEscala(escalaRequest.getCadeiraNome(), escalaRequest.getEscala());
        if(escalaOptional.isPresent()){
            return ResponseEntity.ok(escalaOptional.get());
        }
        throw new EscalaAlreadyExistsException(escalaRequest.getEscala().getDesignation());
    }

    @RequestMapping(value = "/escala", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEscala(@RequestBody EscalaRequest escalaRequest) throws NoEscalaException {
        this.logger.info("Delete Escala Request");

        Boolean deleted = this.escalaService.deleteEscala(escalaRequest);
        if(deleted) {
            return ResponseEntity.ok("Deleted escala " + escalaRequest.getEscala().getDesignation());
        }
        throw new DocenteController.NoEscalaException(escalaRequest.getDesignation());
    }

    @RequestMapping(value = "/escala", method = RequestMethod.PUT)
    public ResponseEntity<Escala> updateEscala(@RequestBody EscalaRequest escalaRequest){
        this.logger.info("Update Request for escala");

        Optional<Escala> optionalEscala =this.escalaService.updateEscala(escalaRequest);
        if(optionalEscala.isPresent()) {
            return ResponseEntity.ok(optionalEscala.get());
        }
        throw new NoEscalaException(escalaRequest.getDesignation());
    }




    //-------------------------------------------//
    //              QUESTAO RELATED              //
    //-------------------------------------------//




    @PostMapping(value = "/questao", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Questao> createQuest(@RequestBody QuestaoRequest questaoRequest){
        this.logger.info("Create Questao Request");

        Optional<Questao> questaoOptional = this.questaoService.createQuestao(questaoRequest.getDocenteNumero(),
                questaoRequest.getCadeiraNome(), questaoRequest.getAno(), questaoRequest.getComp(), questaoRequest.getMomentoNome(),
                questaoRequest.getQuestao(), questaoRequest.getRaNome());
        if(questaoOptional.isPresent()){
            return ResponseEntity.ok(questaoOptional.get());
        }
        throw new QuestaoAlreadyExistsException(questaoRequest.getQuestao().getDesignation());
    }

    @RequestMapping(value = "/questao", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteQuestao(@RequestBody QuestaoRequest questaoRequest) throws NoMomentoException {
        this.logger.info("Delete Request for questao");

        Boolean deleted = this.questaoService.deleteQuestao(questaoRequest.getDocenteNumero(),
                questaoRequest.getCadeiraNome(),
                questaoRequest.getAno(),
                questaoRequest.getComp(),
                questaoRequest.getMomentoNome(),
                questaoRequest.getQuestaoNome());
        if(deleted) {
            return ResponseEntity.ok("Deleted momento " + questaoRequest.getQuestaoNome());
        }
        throw new NoQuestaoException(questaoRequest.getQuestaoNome());
    }

    @RequestMapping(value = "/questao", method = RequestMethod.PUT)
    public ResponseEntity<Questao> updateQuestao(@RequestBody QuestaoRequest questaoRequest){
        this.logger.info("Update Request for questao");

        Optional<Questao> optionalQuestao = this.questaoService.updateQuestao(questaoRequest);
        if(optionalQuestao.isPresent()){
            return ResponseEntity.ok(optionalQuestao.get());
        }
        throw new NoQuestaoException(questaoRequest.getQuestaoNome());
    }



    //-------------------------------------------//
    //              COMPONENTE RELATED           //
    //-------------------------------------------//




    @RequestMapping(value = "/alunoscomp", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Aluno>> listaAlunosComponente(@RequestBody ComponenteRequest componenteRequest){
        this.logger.info("List Alunos of Componente");

        return  ResponseEntity.ok(this.componenteService.listAlunos(componenteRequest));
    }

    @RequestMapping(value = "/alunosoferta", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Aluno>> listaAlunosOferta(@RequestBody OfertaRequest ofertaRequest){
        this.logger.info("List Alunos of Oferta");

        return  ResponseEntity.ok(this.componenteService.listAlunos(ofertaRequest));
    }




    //-------------------------------------------//
    //                NOTAS RELATED              //
    //-------------------------------------------//




    @RequestMapping(value = "/nota", method = RequestMethod.PUT)
    public ResponseEntity<QuestaoRespondida> atribuirNota(@RequestBody NotaRequest notaRequest){
        this.logger.info("Request atribuicao de nota");

        Optional<QuestaoRespondida> optionalQuestaoRespondida =this.questaoRespondidaService.atribuirNota(notaRequest);
        if(optionalQuestaoRespondida.isPresent()) {
            return ResponseEntity.ok(optionalQuestaoRespondida.get());
        }
        throw new NoDocenteException(notaRequest.getDocenteNumero());
    }

    @RequestMapping(value = "/nota/{cadeira}", method = RequestMethod.GET)
    public ResponseEntity<Set<Escala>> notasFinaisComponente(@PathVariable("cadeira") String cadeira, @RequestBody NotaRequest notaRequest){
        this.logger.info("Reqúest notas finais componente");

        Optional<Set<Escala>> optionalNotasFinais =this.docenteService.notasFinaisComponente(cadeira, notaRequest);
        if(optionalNotasFinais.isPresent()) {
            return ResponseEntity.ok(optionalNotasFinais.get());
        }
        throw new NoDocenteException(notaRequest.getDocenteNumero());
    }

    @RequestMapping(value = "/nota", method = RequestMethod.GET)
    public ResponseEntity<Set<Set<Escala>>> notasFinais(@RequestBody NotaRequest notaRequest){
        this.logger.info("Reqúest notas finais componente");

        Optional<Set<Set<Escala>>> optionalNotasFinais =this.docenteService.notasFinais(notaRequest);
        if(optionalNotasFinais.isPresent()) {
            return ResponseEntity.ok(optionalNotasFinais.get());
        }
        throw new NoDocenteException(notaRequest.getDocenteNumero());
    }

    @RequestMapping(value = "/notaMomento", method = RequestMethod.GET)
    public ResponseEntity<Set<Escala>> notaMomentoComponente(@RequestBody NotaRequest notaRequest){
        this.logger.info("Reqúest notas de um momento de uma componente");

        Optional<Set<Escala>> optionalNotas =this.docenteService.notasMomento(notaRequest);
        if(optionalNotas.isPresent()) {
            return ResponseEntity.ok(optionalNotas.get());
        }
        throw new NoDocenteException(notaRequest.getDocenteNumero());
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

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="RA já existente")
    private static class ResultadoAprendizagemAlreadyExistsException extends RuntimeException {

        public ResultadoAprendizagemAlreadyExistsException(String name) {
            super("RA com nome: " + name + " já existe");
        }
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "RA não existente")
    private static class NoResultadoAprendizagemException extends RuntimeException{
        private NoResultadoAprendizagemException(String nome){
            super("RA " + nome + " não existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Escala já existente")
    private static class EscalaAlreadyExistsException extends RuntimeException {

        public EscalaAlreadyExistsException(String name) {
            super("Escala com nome: " + name + " já existe");
        }
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Escala não existente")
    private static class NoEscalaException extends RuntimeException{
        private NoEscalaException(String nome){
            super("Escala " + nome + " não existente");
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
