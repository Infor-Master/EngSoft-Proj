package edu.ufp.esof.projecto.services.filters.Componente;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class ComponenteFilterByTipo implements FilterI<Componente>{

    private String tipo;

    public ComponenteFilterByTipo(String tipo){
        this.tipo =tipo;
    }

    @Override
    public Set<Componente> filter(Set<Componente> entities){
        if(tipo ==null || tipo.isEmpty()){
            return entities;
        }
        Set<Componente> componentes=new HashSet<>();
        for (Componente componente:entities) {
            if(componente.getType()!=null && componente.getType().equals(tipo)){
                componentes.add(componente);
            }
        }
        return componentes;
    }
}