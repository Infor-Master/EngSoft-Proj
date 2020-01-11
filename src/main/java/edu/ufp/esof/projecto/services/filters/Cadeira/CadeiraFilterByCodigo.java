package edu.ufp.esof.projecto.services.filters.Cadeira;

import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class CadeiraFilterByCodigo implements FilterI<Cadeira>{

    private String codigo;

    public CadeiraFilterByCodigo(String codigo){
        this.codigo =codigo;
    }

    @Override
    public Set<Cadeira> filter(Set<Cadeira> entities){
        if(codigo ==null || codigo.isEmpty()){
            return entities;
        }
        Set<Cadeira> cadeiras=new HashSet<>();
        for (Cadeira cadeira:entities) {
            if(cadeira.getCode()!=null && cadeira.getCode().equals(codigo)){
                cadeiras.add(cadeira);
            }
        }
        return cadeiras;
    }
}
