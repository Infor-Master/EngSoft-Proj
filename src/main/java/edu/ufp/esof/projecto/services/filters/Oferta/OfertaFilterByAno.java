package edu.ufp.esof.projecto.services.filters.Oferta;

import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class OfertaFilterByAno implements FilterI<Oferta>{

    private Integer ano;

    public OfertaFilterByAno(Integer ano){
        this.ano=ano;
    }

    @Override
    public Set<Oferta> filter(Set<Oferta> entities){
        if(ano==null){
            return entities;
        }
        Set<Oferta> ofertas=new HashSet<>();
        for (Oferta oferta:entities) {
            if(oferta.getAno()!=null && oferta.getAno().equals(ano)){
                ofertas.add(oferta);
            }
        }
        return ofertas;
    }
}
