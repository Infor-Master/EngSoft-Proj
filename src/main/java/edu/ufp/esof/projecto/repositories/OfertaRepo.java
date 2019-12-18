package edu.ufp.esof.projecto.repositories;

import edu.ufp.esof.projecto.models.Oferta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaRepo extends CrudRepository<Oferta,Long> {
}
