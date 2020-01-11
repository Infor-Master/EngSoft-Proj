package edu.ufp.esof.projecto.services.filters.Cadeira;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.services.filters.AndFilter;
import edu.ufp.esof.projecto.services.filters.FilterI;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterCadeiraService {

    public Set<Cadeira> filter(Set<Cadeira> cadeiras, FilterCadeiraObject filterCadeiraObject){
        FilterI<Cadeira> cadeiraFilterByDesignacao=new CadeiraFilterByDesignacao(filterCadeiraObject.getDesignation());
        FilterI<Cadeira> cadeiraFilterByCodigo=new CadeiraFilterByCodigo(filterCadeiraObject.getCodigo());

        return new AndFilter<>(cadeiraFilterByCodigo, cadeiraFilterByDesignacao).filter(cadeiras);
    }
}
