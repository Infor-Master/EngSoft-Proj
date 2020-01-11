package edu.ufp.esof.projecto.services.filters.Componente;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.services.filters.AndFilter;
import edu.ufp.esof.projecto.services.filters.FilterI;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterComponenteService {

    public Set<Componente> filter(Set<Componente> componentes, FilterComponenteObject filterComponenteObject){
        FilterI<Componente> componenteFilterByCadeira=new ComponenteFilterByCadeira(filterComponenteObject.getCadeira());
        FilterI<Componente> componenteFilterByTipo=new ComponenteFilterByTipo(filterComponenteObject.getTipo());
        FilterI<Componente> componenteFilterByAno=new ComponenteFilterByAno(filterComponenteObject.getAno());

        FilterI<Componente> componenteFilterByCadeiraANDAno=new AndFilter<>(componenteFilterByCadeira, componenteFilterByAno);
        return new AndFilter<>(componenteFilterByCadeiraANDAno, componenteFilterByTipo).filter(componentes);
    }
}
