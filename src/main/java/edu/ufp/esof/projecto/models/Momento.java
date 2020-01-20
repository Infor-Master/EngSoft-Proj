package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.ufp.esof.projecto.models.builders.MomentoRealizadoBuilder;
import edu.ufp.esof.projecto.models.builders.QuestaoBuilder;
import edu.ufp.esof.projecto.models.builders.QuestaoRespondidaBuilder;
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

    public Momento(String designation, Float peso, Componente componente, Set<Questao> questoes) {
        this.designation = designation;
        this.peso = peso;
        this.componente = componente;
        componente.getMomentos().add(this);
        this.questoes = questoes;

        // ANTES
        /*for (Aluno a : this.componente.getAlunos()) {
            MomentoRealizado mr = new MomentoRealizadoBuilder().setAluno(a).setMomento(this)
                    .build();
            for (Questao q : this.getQuestoes()) {
                QuestaoRespondida qr = new QuestaoRespondidaBuilder().setMomento(mr).setQuestao(q).build();
            }
        }*/
    }
}
