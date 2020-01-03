package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlunoRepo extends CrudRepository<Aluno, Long> {
    Optional<Aluno> findByName(String name);
    Optional<Aluno> findByCode(String code);
}
