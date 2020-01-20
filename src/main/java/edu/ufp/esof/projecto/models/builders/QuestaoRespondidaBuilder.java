package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.QuestaoRespondida;

public class QuestaoRespondidaBuilder {

    private MomentoRealizado momento;
    private Criterio criterio;
    private Questao questao;

    public QuestaoRespondidaBuilder setMomento(MomentoRealizado momento) {
        this.momento = momento;
        return this;
    }

    public QuestaoRespondidaBuilder setCriterio(Criterio criterio) {
        this.criterio = criterio;
        return this;
    }

    public QuestaoRespondidaBuilder setQuestao(Questao questao) {
        this.questao = questao;
        return this;
    }

    public QuestaoRespondida build(){
        return new QuestaoRespondida(momento,criterio,questao);
    }
}
