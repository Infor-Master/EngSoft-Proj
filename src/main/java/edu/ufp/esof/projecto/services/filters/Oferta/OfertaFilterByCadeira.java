package edu.ufp.esof.projecto.services.filters.Oferta;

import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class OfertaFilterByCadeira implements FilterI<Oferta>{

    private String cadeiraDes;

    public OfertaFilterByCadeira(String cadeiraDesignation){
        this.cadeiraDes=cadeiraDesignation;
    }

    @Override
    public Set<Oferta> filter(Set<Oferta> entities){
        if(cadeiraDes==null || cadeiraDes.isEmpty()){
            return entities;
        }
        Set<Oferta> ofertas=new HashSet<>();
        for (Oferta oferta:entities) {
            if(oferta.getCadeira()!=null){
                if(oferta.getCadeira().getDesignation()!=null && oferta.getCadeira().getDesignation().equals(cadeiraDes)){
                    ofertas.add(oferta);
                }
            }
        }
        return ofertas;
    }
}
