package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Docente docente;

    @ManyToMany
    @JsonBackReference
    private Set<Aluno> alunos=new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Oferta oferta;

    @OneToMany(mappedBy = "componente",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Momento> momentos=new HashSet<>();


    public Componente(String type) {
        this.setType(type);
    }

}