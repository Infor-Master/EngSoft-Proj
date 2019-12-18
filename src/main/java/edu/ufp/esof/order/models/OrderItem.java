package edu.ufp.esof.order.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private LocalDate orderDate;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "order",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<LineOrder> lineOrders=new HashSet<>();

    public OrderItem(){
        this.orderDate=LocalDate.now();
    }

    public OrderItem(String orderNumber) {
        this();
        this.orderNumber=orderNumber;
    }

    public OrderItem(Long id, String orderNumber, LocalDate orderDate, Client client, Set<LineOrder> lineOrders) {
        this.setId(id);
        this.setOrderNumber(orderNumber);
        this.setOrderDate(orderDate);
        this.setClient(client);
        this.setLineOrders(lineOrders);
    }

    public void addLineOrder(LineOrder lineOrder){
        this.lineOrders.add(lineOrder);
        lineOrder.setOrder(this);
    }

    @ToString.Include
    public float price(){
        float price=0;
        for(LineOrder lineOrder:this.lineOrders){
            price+=lineOrder.getQuantity()*lineOrder.getProduct().getPrice();
        }
        return price;
    }


}
