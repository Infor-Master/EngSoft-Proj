package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Componente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponenteRepo extends CrudRepository<Componente, Long> {
    Optional<Componente> findByType(String type);
    //@Query("SELECT * FROM COMPONENTE c, CADEIRA_OFERTAS cao, Oferta o, Cadeira cadeira where cadeira.id = :id and ")
}
