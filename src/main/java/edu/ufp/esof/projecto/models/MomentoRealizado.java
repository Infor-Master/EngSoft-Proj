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
public class MomentoRealizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float grade;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference(value = "alunos-momentos")
    private Aluno aluno;

    @OneToMany(mappedBy = "momento",cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<QuestaoRespondida> questoes=new HashSet<>();


    @OneToOne
    //@JsonManagedReference
    private Momento momento;

    public MomentoRealizado(Momento momento){
        this.setMomento(momento);
    }
}
