package edu.ufp.esof.projecto.services.filters.Docente;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Data
public class FilterDocenteObject {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String cadeira;
    private Integer ano;

    public FilterDocenteObject(Map<String, String> params){
        Integer ano=null;
        try {
            ano=Integer.parseInt(params.get("ano"));
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }

        this.cadeira=params.get("cadeira");
        this.ano=ano;
    }

}
