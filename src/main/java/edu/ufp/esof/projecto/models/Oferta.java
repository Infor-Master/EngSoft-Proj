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
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ano;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private Cadeira cadeira;

    //@OneToMany(mappedBy = "oferta",cascade = CascadeType.PERSIST)
    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Componente> componentes=new HashSet<>();

    //@OneToMany(mappedBy = "oferta",cascade = CascadeType.PERSIST)
    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<ResultadoAprendizagem> ras=new HashSet<>();

    public Oferta(Integer ano, Cadeira cadeira) {
        this.ano = ano;
        this.cadeira = cadeira;
        cadeira.getOfertas().add(this);
    }
}