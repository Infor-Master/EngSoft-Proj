package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Escala;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.CadeiraService;
import edu.ufp.esof.projecto.services.ComponenteService;
import edu.ufp.esof.projecto.services.CriterioService;
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
@RequestMapping("/cadeira")
public class CadeiraController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CadeiraService cadeiraService;
    private ComponenteService componenteService;
    private CriterioService criterioService;
    private OfertaService ofertaService;

    @Autowired
    public CadeiraController(CadeiraService cadeiraService, ComponenteService componenteService, CriterioService criterioService, OfertaService ofertaService) {
        this.cadeiraService = cadeiraService;
        this.componenteService = componenteService;
        this.criterioService = criterioService;
        this.ofertaService = ofertaService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Cadeira>> getAllCadeiras(){
        this.logger.info("Listing Request for cadeiras");

        return ResponseEntity.ok(this.cadeiraService.findAll());
    }

    @RequestMapping(value = "/{nome}",method = RequestMethod.GET)
    public ResponseEntity<Cadeira> getCadeiraByName(@PathVariable("nome") String nome) throws NoCadeiraException {
        this.logger.info("Listing Request for cadeira named " + nome);

        Optional<Cadeira> optionalCadeira =this.cadeiraService.findByName(nome);
        if(optionalCadeira.isPresent()) {
            return ResponseEntity.ok(optionalCadeira.get());
        }
        throw new NoNamedCadeiraException(nome);
    }

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Cadeira>> searchCadeiras(@RequestParam Map<String,String> query){
        return ResponseEntity.ok(this.cadeiraService.filterCadeira(query));
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




    //-------------------------------------------//
    //             COMPONENTE RELATED            //
    //-------------------------------------------//




    @RequestMapping(value = "/{cadeira}/{ano}/componentes", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Componente>> getAllComponentes(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano){
        this.logger.info("Listing Request for " + cadeira + "'s Componentes in " + ano);

        return ResponseEntity.ok(this.componenteService.findAll(cadeira, ano));
    }


    @RequestMapping(value = "/{cadeira}/{ano}/{type}",method = RequestMethod.GET)
    public ResponseEntity<Componente> getComponente(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("type") String type) throws NoComponenteException {
        this.logger.info("Listing Request for component " + type + " of " + cadeira + " in " + ano);

        Optional<Componente> optionalComponente =this.componenteService.findByType(cadeira, ano, type);
        if(optionalComponente.isPresent()) {
            return ResponseEntity.ok(optionalComponente.get());
        }
        throw new NoComponenteException(type);
    }

    @RequestMapping(value = "/{cadeira}/{ano}/{type}", method = RequestMethod.PUT)
    public ResponseEntity<Componente>editComponente(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("type") String type, @RequestBody Componente componente) throws NoComponenteException{
        this.logger.info("Update Request for component " + type + " of " + cadeira + " in " + ano);

        Optional<Componente> optionalComponente =this.componenteService.updateComponente(cadeira,ano,type, componente);
        if(optionalComponente.isPresent()) {
            return ResponseEntity.ok(optionalComponente.get());
        }
        throw new NoComponenteException(type);
    }

    @RequestMapping(value = "/{cadeira}/{ano}/componentes", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllComponentes(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano){
        this.logger.info("Delete Request for every componente of " + cadeira + " in " + ano);

        componenteService.deleteAll(cadeira, ano);
        return ResponseEntity.ok("Deleted every componente");
    }

    @RequestMapping(value = "/{cadeira}/{ano}/{type}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteComponente(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @PathVariable("type") String type) throws NoComponenteException{
        this.logger.info("Delete Request for componente " + type + " of " + cadeira +" in " + ano);

        Boolean deleted = this.componenteService.deleteComponente(cadeira,ano,type);
        if(deleted) {
            return ResponseEntity.ok("Delete componente " + type);
        }
        throw new NoComponenteException(type);
    }

    @PostMapping(value = "/{cadeira}/{ano}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Componente> createComponente(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @RequestBody Componente componente){
        this.logger.info("Create Componente Request");

        Optional<Componente> componenteOptional=this.componenteService.createComponente(cadeira,ano,componente);
        if(componenteOptional.isPresent()){
            return ResponseEntity.ok(componenteOptional.get());
        }
        throw new ComponenteAlreadyExistsExcpetion(componente.getType());
    }




    //-------------------------------------------//
    //             CRITERIO RELATED              //
    //-------------------------------------------//




    @RequestMapping(value = "/{cadeira}/criterios", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Escala>> getAllCriterios(@PathVariable("cadeira") String cadeira){
        this.logger.info("Listing Request for escalas");

        return ResponseEntity.ok(this.criterioService.findAll(cadeira));
    }

    @RequestMapping(value = "/{cadeira}/criterios/{designation}",method = RequestMethod.GET)
    public ResponseEntity<Escala> getCriterio(@PathVariable("cadeira") String cadeira, @PathVariable("designation") String designation) throws NoCriterioExcpetion {
        this.logger.info("Listing Request for escala " + designation + " of " + cadeira);

        Optional<Escala> optionalCriterio =this.criterioService.findByDesignation(cadeira, designation);
        if(optionalCriterio.isPresent()) {
            return ResponseEntity.ok(optionalCriterio.get());
        }
        throw new NoCriterioExcpetion(designation);
    }

    @RequestMapping(value = "/{cadeira}/criterios/{designation}", method = RequestMethod.PUT)
    public ResponseEntity<Escala>editCriterio(@PathVariable("cadeira") String cadeira, @PathVariable("designation") String designation, @RequestBody Escala escala) throws NoCriterioExcpetion{
        this.logger.info("Update Request for escala " + designation + " of " + cadeira);

        Optional<Escala> optionalCriterio =this.criterioService.updateCriterio(cadeira, designation, escala);
        if(optionalCriterio.isPresent()) {
            return ResponseEntity.ok(optionalCriterio.get());
        }
        throw new NoCriterioExcpetion(designation);
    }

    @RequestMapping(value = "/{cadeira}/criterios/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllCriterios(@PathVariable("cadeira") String cadeira){
        this.logger.info("Delete Request for every escala of " + cadeira);

        criterioService.deleteAll(cadeira);
        return ResponseEntity.ok("Deleted every escala from " + cadeira);
    }

    @RequestMapping(value = "/{cadeira}/criterios/{designation}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCriterio(@PathVariable("cadeira") String cadeira, @PathVariable("designation") String designation) throws NoCriterioExcpetion{
        this.logger.info("Delete Request for escala " + designation + " from " + cadeira);

        Boolean deleted = this.criterioService.deleteCriterio(cadeira, designation);
        if(deleted) {
            return ResponseEntity.ok("Delete escala " + designation);
        }
        throw new NoCriterioExcpetion(designation);
    }

    @PostMapping(value = "/{cadeira}/criterios", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Escala> createCriterio(@PathVariable("cadeira") String cadeira, @RequestBody Escala escala){
        this.logger.info("Create Escala Request for " + cadeira);

        Optional<Escala> criterioOptional=this.criterioService.createCriterio(cadeira, escala);
        if(criterioOptional.isPresent()){
            return ResponseEntity.ok(criterioOptional.get());
        }
        throw new CriterioAlreadyExistsExcpetion(escala.getDesignation());
    }




    //-------------------------------------------//
    //              OFERTA RELATED               //
    //-------------------------------------------//




    // Perguntar ao professor
    @RequestMapping(value = "/{cadeira}/ofertas", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Oferta>> getAllOfertas(@PathVariable("cadeira") String cadeira){
        this.logger.info("Listing Request for ofertas for " + cadeira);
        return ResponseEntity.ok(this.ofertaService.findAll(cadeira));
    }

    @RequestMapping(value = "/{cadeira}/{ano}", method = RequestMethod.GET)
    public ResponseEntity<Oferta> getOfertaById(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano) throws NoOfertaException{
        this.logger.info("Listing Request for oferta " + ano + " from " + cadeira);
        Optional<Oferta> optionalOferta=this.ofertaService.find(cadeira, ano);
        if(optionalOferta.isPresent()){
            return ResponseEntity.ok(optionalOferta.get());
        }
        throw new NoOfertaException(cadeira, ano);
    }

    @RequestMapping(value = "/{cadeira}/{ano}", method = RequestMethod.PUT)
    public ResponseEntity<Oferta>editOferta(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano, @RequestBody Oferta oferta) throws NoOfertaException{
        this.logger.info("Update Request for oferta " + ano + " from " + cadeira);

        Optional<Oferta> optionalOferta=this.ofertaService.updateOferta(cadeira, ano, oferta);
        if(optionalOferta.isPresent()){
            return ResponseEntity.ok(optionalOferta.get());
        }
        throw new NoOfertaException(cadeira, ano);
    }

    @RequestMapping(value = "/{cadeira}/ofertas", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllOfertas(@PathVariable("cadeira") String cadeira){
        this.logger.info("Delete Request for every oferta from " + cadeira);

        ofertaService.deleteAll(cadeira);
        return ResponseEntity.ok("Deleted every oferta from " + cadeira);
    }

    @RequestMapping(value = "/{cadeira}/{ano}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteOferta(@PathVariable("cadeira") String cadeira, @PathVariable("ano") int ano) throws NoOfertaException{
        this.logger.info("Delete Request for oferta " + ano + " from " + cadeira);

        Boolean deleted = this.ofertaService.deleteOferta(cadeira, ano);
        if(deleted){
            return ResponseEntity.ok("Delete oferta " + ano + " from " + cadeira);
        }
        throw new NoOfertaException(cadeira, ano);
    }

    @PostMapping(value = "/{cadeira}/ofertas", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Oferta> createOferta(@PathVariable("cadeira") String cadeira, @RequestBody Oferta oferta){
        this.logger.info("Create Oferta Request with ano = " + oferta.getAno() + " for cadeira " + cadeira);

        Optional<Oferta> optionalOferta=this.ofertaService.createOferta(cadeira, oferta);
        if(optionalOferta.isPresent()){
            return ResponseEntity.ok(optionalOferta.get());
        }
        throw new OfertaAlreadyExistsException(oferta.getId());
    }




    //-------------------------------------------//
    //                 EXCEPTIONS                //
    //-------------------------------------------//




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

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Escala não existente")
    private static class NoCriterioExcpetion extends RuntimeException {

        private NoCriterioExcpetion(String designation) {
            super("Escala " + designation + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Escala já existente")
    private static class CriterioAlreadyExistsExcpetion extends RuntimeException {

        public CriterioAlreadyExistsExcpetion(String designation) {
            super("Escala " + designation + " já existe");
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Oferta não existente")
    private static class NoOfertaException extends RuntimeException{
        private NoOfertaException(String cadeira, int ano){super("Oferta de " + cadeira + " no ano " + ano + "nao existente");}
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Oferta já existente")
    private static class OfertaAlreadyExistsException extends RuntimeException{
        public OfertaAlreadyExistsException(Long id){super("Oferta com o id : " + id + " já existe");}
    }
}
