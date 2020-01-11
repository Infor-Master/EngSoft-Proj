package edu.ufp.esof.projecto.services.filters.Cadeira;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Data
public class FilterCadeiraObject {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String codigo;
    private String designation;

    public FilterCadeiraObject(Map<String, String> params){
        this.codigo=params.get("codigo");
        this.designation=params.get("designation");
    }

}
