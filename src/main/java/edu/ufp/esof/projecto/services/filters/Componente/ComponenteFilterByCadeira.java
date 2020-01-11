package edu.ufp.esof.projecto.services.filters.Componente;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class ComponenteFilterByCadeira implements FilterI<Componente>{

    private String cadeira;

    public ComponenteFilterByCadeira(String cadeira){
        this.cadeira = cadeira;
    }

    @Override
    public Set<Componente> filter(Set<Componente> entities){
        if(cadeira ==null || cadeira.isEmpty()){
            return entities;
        }
        Set<Componente> componentes=new HashSet<>();
        for (Componente componente:entities) {
            if(componente.getOferta()!=null){
                if(componente.getOferta().getCadeira()!=null){
                    if(componente.getOferta().getCadeira().getDesignation()!=null && componente.getOferta().getCadeira().getDesignation().equals(cadeira)){
                        componentes.add(componente);
                    }
                }
            }
        }
        return componentes;
    }
}
