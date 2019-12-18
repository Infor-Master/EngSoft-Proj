package edu.ufp.esof.order.controllers;

import edu.ufp.esof.order.models.OrderItem;
import edu.ufp.esof.order.services.OrderServiceAbstraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final String WORD_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private OrderServiceAbstraction orderService;

    @Autowired
    public OrderController(OrderServiceAbstraction orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<OrderItem>> getAllOrders() {
        return ResponseEntity.ok(this.orderService.findAll());
    }

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<OrderItem>> searchOrders(@RequestParam Map<String,String> searchParams){
        return ResponseEntity.ok(this.orderService.filterOrders(searchParams));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderItem> createOrder(@RequestBody OrderItem order) {
        Optional<OrderItem> orderItemOptional=orderService.createOrder(order);
        if(orderItemOptional.isPresent()) {
            return ResponseEntity.ok(orderItemOptional.get());
        }
        throw new BadOrderExcpetion();
    }

    @GetMapping(value="/{format}/{id}", produces = {OrderController.WORD_MIME_TYPE,MediaType.ALL_VALUE} )
    public ResponseEntity<byte[]> getFile(
            @RequestHeader(value="Authorization") String token,
            @PathVariable Long id, @PathVariable String format) {

        Optional<OrderItem> optionalOrderItem=this.orderService.accessOrder(id,token);
        if(optionalOrderItem.isEmpty()){
            throw new NoOrderExcpetion(id);
        }
        OrderItem order=optionalOrderItem.get();

        byte[] fileStream = this.orderService.outputFile(order,format);
        String filename = "order" + order.getId() +"." +format;
        HttpHeaders headers=getHeaders(format,filename);
        return new ResponseEntity<>(fileStream, headers, HttpStatus.OK);
    }

    private HttpHeaders getHeaders(String type,String filename){
        HttpHeaders headers = new HttpHeaders();
        if(type.equalsIgnoreCase("pdf")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        }else{
            headers.setContentType(MediaType.valueOf(WORD_MIME_TYPE));
        }
        headers.setContentDispositionFormData(filename, filename);
        return headers;
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No order")
    private static class NoOrderExcpetion extends RuntimeException {
        public NoOrderExcpetion(Long id) {
            super("No such order with id: "+id);
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Cannot order")
    private static class BadOrderExcpetion extends RuntimeException {
        public BadOrderExcpetion() {
            super("Cannot create order ");
        }
    }
}
