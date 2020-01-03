package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIdentityInfo(
        generator= ObjectIdGenerators.PropertyGenerator.class,
        property="id")
@NoArgsConstructor
public class Momento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private Float peso;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private Componente componente;

    @OneToMany(mappedBy = "momento", cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Questao> questoes=new HashSet<>();

    public Momento(String designation){
        this.setDesignation(designation);
    }
}
