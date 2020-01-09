package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.services.AlunoService;
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
@RequestMapping("/aluno")
public class AlunoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private AlunoService alunoService;

    @Autowired
    public AlunoController(AlunoService alunoService){
        this.alunoService = alunoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Aluno>> getAllAlunos(){
        this.logger.info("Listing Request for alunos");

        return ResponseEntity.ok(this.alunoService.findAll());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Aluno> getAlunoById(@PathVariable("id") String id) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for aluno with id " + id);

        Optional<Aluno> optionalAluno =this.alunoService.findByNumber(id);
        if(optionalAluno.isPresent()) {
            return ResponseEntity.ok(optionalAluno.get());
        }
        throw new NoAlunoExcpetion(id);
    }

    //@PutMapping(value = "{id}")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    //@PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aluno>editAluno(@PathVariable("id") String id, @RequestBody Aluno aluno) throws NoAlunoExcpetion{
        this.logger.info("Update Request for aluno with id " + id);

        Optional<Aluno> optionalAluno =this.alunoService.updateAluno(id, aluno);
        if(optionalAluno.isPresent()) {
            return ResponseEntity.ok(optionalAluno.get());
        }
        throw new NoAlunoExcpetion(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllAlunos(){
        this.logger.info("Delete Request for every aluno");

        alunoService.deleteAll();
        return ResponseEntity.ok("Deleted every aluno");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAluno(@PathVariable("id") String id) throws NoAlunoExcpetion{
        this.logger.info("Delete Request for aluno with id " + id);

        Boolean deleted = this.alunoService.deleteAluno(id);
        if(deleted) {
            return ResponseEntity.ok("Delete aluno with id = " + id);
        }
        throw new NoAlunoExcpetion(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno){
        this.logger.info("Create Aluno Request with name = " + aluno.getName() + " and code = " + aluno.getCode());

        Optional<Aluno> alunoOptional=this.alunoService.createAluno(aluno);
        if(alunoOptional.isPresent()){
            return ResponseEntity.ok(alunoOptional.get());
        }
        throw new AlunoAlreadyExistsExcpetion(aluno.getName());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Aluno não existente")
    private static class NoAlunoExcpetion extends RuntimeException {

        private NoAlunoExcpetion(String id) {
            super("Aluno com id " + id + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Aluno já existente")
    private static class AlunoAlreadyExistsExcpetion extends RuntimeException {

        public AlunoAlreadyExistsExcpetion(String name) {
            super("Aluno com nome: " + name + " já existe");
        }
    }

    // Falta filtros por cadeira e por cadeira e numero de aluno
    // falta PUT e DELETE

}