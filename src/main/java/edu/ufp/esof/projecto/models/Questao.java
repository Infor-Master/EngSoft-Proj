package edu.ufp.esof.projecto.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.ufp.esof.projecto.models.builders.QuestaoRespondidaBuilder;
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
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private Float pesoMomento;
    private Float pesoRA;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private Momento momento;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private ResultadoAprendizagem ra;

    public Questao(String designation, Float pesoMomento, Float pesoRA){
        this.setDesignation(designation);
        this.setPesoMomento(pesoMomento);
        this.setPesoRA(pesoRA);
    }

    public Questao(String designation, Float pesoMomento, Float pesoRA, Momento momento, ResultadoAprendizagem ra) {
        this.designation = designation;
        this.pesoMomento = pesoMomento;
        this.pesoRA = pesoRA;
        this.momento = momento;
        momento.getQuestoes().add(this);
        this.ra = ra;
        ra.getQuestoes().add(this);

        // ANTES
        /*for (Aluno a : momento.getComponente().getAlunos()) {
            for (MomentoRealizado mr : a.getMomentos()){
                if (mr.getMomento().getId() == momento.getId()){
                    mr.getQuestoes().add(new QuestaoRespondidaBuilder().setMomento(mr).setQuestao(this).build());
                    break;
                }
            }
        }*/
    }
}
