package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@JsonIdentityInfo(
        generator= ObjectIdGenerators.PropertyGenerator.class,
        property="id")
@NoArgsConstructor
public class QuestaoRespondida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private MomentoRealizado momento;

    @OneToOne
    //@JsonManagedReference
    private Escala escala;

    @OneToOne
    //@JsonManagedReference
    private Questao questao;

    public QuestaoRespondida(Questao questao){
        this.setQuestao(questao);
    }

    public QuestaoRespondida(MomentoRealizado momento, Escala escala, Questao questao) {
        this.momento = momento;
        momento.getQuestoes().add(this);
        this.escala = escala;
        this.questao = questao;
    }
}
