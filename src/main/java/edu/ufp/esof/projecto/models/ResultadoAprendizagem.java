package edu.ufp.esof.projecto.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
public class ResultadoAprendizagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private Oferta oferta;

    @OneToMany(mappedBy = "ra", cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Questao> questoes=new HashSet<>();

    public ResultadoAprendizagem(String designation){
        this.setDesignation(designation);
    }

    public ResultadoAprendizagem(String designation, Oferta oferta, Set<Questao> questoes) {
        this.designation = designation;
        this.oferta = oferta;
        oferta.getRas().add(this);
        this.questoes = questoes;
    }
}
