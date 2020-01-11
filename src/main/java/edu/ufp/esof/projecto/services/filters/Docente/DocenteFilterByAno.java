package edu.ufp.esof.projecto.services.filters.Docente;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Docente;
import edu.ufp.esof.projecto.services.filters.FilterI;

import java.util.HashSet;
import java.util.Set;

public class DocenteFilterByAno implements FilterI<Docente>{

    private Integer ano;

    public DocenteFilterByAno(Integer ano){
        this.ano=ano;
    }

    @Override
    public Set<Docente> filter(Set<Docente> entities){
        if(ano==null){
            return entities;
        }
        Set<Docente> docentes=new HashSet<>();
        for (Docente docente:entities) {
            if(docente.getComponentes()!=null){
                for (Componente cmp:docente.getComponentes()) {
                    if(cmp.getOferta()!=null && cmp.getOferta().getAno().equals(ano)){
                        docentes.add(docente);
                    }
                }
            }
        }
        return docentes;
    }
}
