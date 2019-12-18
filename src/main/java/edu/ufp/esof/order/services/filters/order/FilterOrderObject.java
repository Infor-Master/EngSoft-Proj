package edu.ufp.esof.order.services.filters.order;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;


@Data
public class FilterOrderObject {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String productName;
    private String clientName;

    public FilterOrderObject(String clientName, String productName, LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.productName = productName;
        this.clientName = clientName;
    }

    public FilterOrderObject() {
    }

    public FilterOrderObject(Map<String, String> searchParams) {
        this();
        this.productName=searchParams.get("product");
        this.clientName=searchParams.get("client");

        String startDate=searchParams.get("startDate");
        String endDate=searchParams.get("endDate");
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