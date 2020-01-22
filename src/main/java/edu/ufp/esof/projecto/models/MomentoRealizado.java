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

    public MomentoRealizado(Float grade, Aluno aluno, Set<QuestaoRespondida> questoes, Momento momento) {
        this.grade = grade;
        this.aluno = aluno;
        aluno.getMomentos().add(this);
        this.questoes = questoes;
        this.momento = momento;
    }

    public float notaRa(ResultadoAprendizagem ra){
        float nota = 0.0f;
        for (QuestaoRespondida qr : questoes) {
            if (qr.getEscala()!= null && ra.getDesignation().equals(qr.getQuestao().getRa().getDesignation())){
                nota += qr.getQuestao().getPesoRA()*qr.getEscala().getNota();
            }
        }
        return nota;
    }
    
    public float nota(){
        float nota = 0.0f;
        for (QuestaoRespondida qr : questoes) {
            if (qr.getEscala()!= null){
                nota += qr.getQuestao().getPesoMomento()*qr.getEscala().getNota();
            }
        }
        return nota;
    }
}
