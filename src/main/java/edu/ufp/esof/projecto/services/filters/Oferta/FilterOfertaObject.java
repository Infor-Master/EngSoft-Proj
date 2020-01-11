package edu.ufp.esof.projecto.services.filters.Oferta;

import edu.ufp.esof.projecto.models.Cadeira;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Data
public class FilterOfertaObject {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer ano;
    private String cadeiraDesignation;

    public FilterOfertaObject(Map<String, String> params){
        String anoString=params.get("ano");
        String cadeira=params.get("cadeira");

        Integer ano=null;

        try{
            ano=Integer.parseInt(anoString);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }

        this.ano=ano;
        this.cadeiraDesignation=cadeira;
    }

}
