package edu.ufp.esof.order.services.filters.client;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

@Data
public class FilterClientObject {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    public FilterClientObject(Map<String,String> params) {
        this.name=params.get("name");

        String startDate=params.get("startDate");
        String endDate=params.get("endDate");

        LocalDate date1=null;
        LocalDate date2=null;

        try{
            date1=LocalDate.parse(startDate);
            date2=LocalDate.parse(endDate);
        }catch (Exception e){
            //e.printStackTrace();
            this.logger.error(e.getMessage());
        }

        this.startDate=date1;
        this.endDate=date2;
    }
}
