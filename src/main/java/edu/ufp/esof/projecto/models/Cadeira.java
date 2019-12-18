package edu.ufp.esof.projecto.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Cadeira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private String code;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Oferta> ofertas = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Criterio> criterios = new HashSet<>();

    public Cadeira(String designation, String code){
        this.setDesignation(designation);
        this.setCode(code);
    }
}
