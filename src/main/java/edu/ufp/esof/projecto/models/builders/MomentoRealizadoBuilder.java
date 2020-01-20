package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.MomentoRealizado;
import edu.ufp.esof.projecto.models.QuestaoRespondida;

import java.util.HashSet;
import java.util.Set;

public class MomentoRealizadoBuilder {

    private Float grade;
    private Aluno aluno;
    private Set<QuestaoRespondida> questoes=new HashSet<>();
    private Momento momento;

    public MomentoRealizadoBuilder setGrade(Float grade) {
        this.grade = grade;
        return this;
    }

    public MomentoRealizadoBuilder setAluno(Aluno aluno) {
        this.aluno = aluno;
        return this;
    }

    public MomentoRealizadoBuilder setQuestoes(Set<QuestaoRespondida> questoes) {
        this.questoes = questoes;
        return this;
    }

    public MomentoRealizadoBuilder addQuestao(QuestaoRespondida qr){
        this.questoes.add(qr);
        return this;
    }

    public MomentoRealizadoBuilder setMomento(Momento momento) {
        this.momento = momento;
        return this;
    }

    public MomentoRealizado build(){
        return new MomentoRealizado(grade,aluno,questoes,momento);
    }
}
