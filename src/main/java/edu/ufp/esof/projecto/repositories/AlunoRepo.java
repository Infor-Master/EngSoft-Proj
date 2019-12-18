package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepo extends CrudRepository<Aluno, Long> {
}
