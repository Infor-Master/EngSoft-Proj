package edu.ufp.esof.projecto.services.filters.Oferta;

import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.filters.AndFilter;
import edu.ufp.esof.projecto.services.filters.FilterI;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterOfertaService {

    public Set<Oferta> filter(Set<Oferta> ofertas, FilterOfertaObject filterOfertaObject){
        FilterI<Oferta> ofertaFilterByAno=new OfertaFilterByAno(filterOfertaObject.getAno());
        FilterI<Oferta> ofertaFilterByCadeira=new OfertaFilterByCadeira(filterOfertaObject.getCadeiraDesignation());

        return new AndFilter<>(ofertaFilterByAno, ofertaFilterByCadeira).filter(ofertas);
    }
}
