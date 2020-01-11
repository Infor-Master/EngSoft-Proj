package edu.ufp.esof.projecto.services.filters.Docente;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Docente;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class DocenteFilterByCadeira implements FilterI<Docente>{

    private String cadeira;

    public DocenteFilterByCadeira(String cadeira){
        this.cadeira=cadeira;
    }

    @Override
    public Set<Docente> filter(Set<Docente> entities){
        if(cadeira==null || cadeira.isEmpty()){
            return entities;
        }
        Set<Docente> docentes=new HashSet<>();
        for (Docente docente:entities) {
            if(docente.getComponentes()!=null){
                for (Componente cmp:docente.getComponentes()) {
                    if(cmp.getOferta()!=null){
                        if(cmp.getOferta().getCadeira()!=null && cmp.getOferta().getCadeira().getDesignation().equals(cadeira)){
                            docentes.add(docente);
                        }
                    }
                }
            }
        }
        return docentes;
    }
}
