package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class QuestaoRespondida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private MomentoRealizado momento;

    @OneToOne
    @JsonManagedReference
    private Criterio criterio;

    @OneToOne
    @JsonManagedReference
    private Questao questao;

    public QuestaoRespondida(Questao questao){
        this.setQuestao(questao);
    }

}
