package edu.ufp.esof.projecto.services.filters.Cadeira;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class CadeiraFilterByDesignacao implements FilterI<Cadeira>{

    private String designation;

    public CadeiraFilterByDesignacao(String designation){
        this.designation=designation;
    }

    @Override
    public Set<Cadeira> filter(Set<Cadeira> entities){
        if(designation==null || designation.isEmpty()){
            return entities;
        }
        Set<Cadeira> cadeiras=new HashSet<>();
        for (Cadeira cadeira:entities) {
            if(cadeira.getDesignation()!=null && cadeira.getDesignation().equals(designation)){
                cadeiras.add(cadeira);
            }
        }
        return cadeiras;
    }
}
