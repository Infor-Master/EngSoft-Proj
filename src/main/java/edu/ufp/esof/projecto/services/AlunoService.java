package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.repositories.AlunoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class AlunoService {

    private AlunoRepo alunoRepo;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public AlunoService(AlunoRepo alunoRepo) {
        this.alunoRepo = alunoRepo;
    }

    public Set<Aluno> findAll() {
        Set<Aluno> alunos=new HashSet<>();
        for(Aluno a:this.alunoRepo.findAll()){
            alunos.add(a);
        }
        return alunos;
    }

    public Optional<Aluno> findByNumber(String id) {
        Optional<Aluno> optionalAluno = Optional.empty();
        for(Aluno a:this.alunoRepo.findAll()){
            if (a.getCode().compareTo(id) == 0){
                optionalAluno = Optional.of(a);
                break;
            }
        }
        return optionalAluno;
    }

    public Optional<Aluno> createAluno(Aluno aluno) {
        Optional<Aluno> optionalAluno=this.alunoRepo.findByCode(aluno.getCode());
        if(optionalAluno.isPresent()){
            return Optional.empty();
        }
        Aluno createdAluno=this.alunoRepo.save(aluno);
        return Optional.of(createdAluno);
    }

    public Optional<Aluno> updateAluno(String code, Aluno aluno){
        Optional<Aluno> optionalAluno=this.alunoRepo.findByCode(code);
        if(optionalAluno.isPresent()){
            alunoRepo.save(aluno);
            return optionalAluno;
        }
        return Optional.empty();
    }

    public Boolean deleteAluno(String code){
        Optional<Aluno> optionalAluno=this.alunoRepo.findByCode(code);
        if(optionalAluno.isPresent()){
            alunoRepo.delete(optionalAluno.get());
            return true;
        }
        return false;
    }

    public void deleteAll(){
        alunoRepo.deleteAll();
    }
}
