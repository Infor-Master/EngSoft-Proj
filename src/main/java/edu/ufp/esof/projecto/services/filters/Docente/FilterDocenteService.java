package edu.ufp.esof.projecto.services.filters.Docente;

import edu.ufp.esof.projecto.models.Docente;
import edu.ufp.esof.projecto.services.filters.AndFilter;
import edu.ufp.esof.projecto.services.filters.FilterI;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterDocenteService {

    public Set<Docente> filter(Set<Docente> docentes, FilterDocenteObject filterDocenteObject){
        FilterI<Docente> docenteFilterByCadeira=new DocenteFilterByCadeira(filterDocenteObject.getCadeira());
        FilterI<Docente> docenteFilterByAno=new DocenteFilterByAno(filterDocenteObject.getAno());


        return new AndFilter<>(docenteFilterByAno, docenteFilterByCadeira).filter(docentes);
    }
}
