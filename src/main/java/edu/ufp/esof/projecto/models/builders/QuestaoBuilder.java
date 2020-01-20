package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.ResultadoAprendizagem;

public class QuestaoBuilder {

    private String designation;
    private Float pesoMomento;
    private Float pesoRA;
    private Momento momento;
    private ResultadoAprendizagem ra;

    public QuestaoBuilder setDesignation(String designation) {
        this.designation = designation;
        return this;
    }

    public QuestaoBuilder setPesoMomento(Float pesoMomento) {
        this.pesoMomento = pesoMomento;
        return this;
    }

    public QuestaoBuilder setPesoRA(Float pesoRA) {
        this.pesoRA = pesoRA;
        return this;
    }

    public QuestaoBuilder setMomento(Momento momento) {
        this.momento = momento;
        return this;
    }

    public QuestaoBuilder setRa(ResultadoAprendizagem ra) {
        this.ra = ra;
        return this;
    }

    public Questao build(){
        return new Questao(designation,pesoMomento,pesoRA,momento,ra);
    }
}
