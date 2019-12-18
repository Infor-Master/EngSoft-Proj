package edu.ufp.esof.order.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "supplier",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Product> products=new HashSet<>();

    public Supplier(String name) {
        this.setName(name);
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setSupplier(this);
    }
}
