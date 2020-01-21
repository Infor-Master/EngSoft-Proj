package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Escala;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.QuestaoRespondida;

public class QuestaoRespondidaBuilder {

    private MomentoRealizado momento;
    private Escala escala;
    private Questao questao;

    public QuestaoRespondidaBuilder setMomento(MomentoRealizado momento) {
        this.momento = momento;
        return this;
    }

    public QuestaoRespondidaBuilder setEscala(Escala escala) {
        this.escala = escala;
        return this;
    }

    public QuestaoRespondidaBuilder setQuestao(Questao questao) {
        this.questao = questao;
        return this;
    }

    public QuestaoRespondida build(){
        return new QuestaoRespondida(momento, escala,questao);
    }
}
