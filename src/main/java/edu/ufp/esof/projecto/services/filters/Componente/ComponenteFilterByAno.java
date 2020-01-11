package edu.ufp.esof.projecto.services.filters.Componente;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class ComponenteFilterByAno implements FilterI<Componente>{

    private Integer ano;

    public ComponenteFilterByAno(Integer ano){
        this.ano =ano;
    }

    @Override
    public Set<Componente> filter(Set<Componente> entities){
        if(ano ==null){
            return entities;
        }
        Set<Componente> componentes=new HashSet<>();
        for (Componente componente:entities) {
            if(componente.getOferta()!=null){
                if(componente.getOferta().getAno()!=null && componente.getOferta().getAno().equals(ano)){
                    componentes.add(componente);
                }
            }
        }
        return componentes;
    }
}
