package edu.ufp.esof.projecto.services.filters.Componente;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Data
public class FilterComponenteObject {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String tipo;
    private Integer ano; //oferta
    private String cadeira; //designation

    public FilterComponenteObject(Map<String, String> params){
        this.tipo=params.get("tipo");
        this.cadeira=params.get("cadeira");
        Integer ano = null;

        try {
            ano=Integer.parseInt(params.get("ano"));
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
        this.ano=ano;
    }

}
