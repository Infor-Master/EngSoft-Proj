package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.Questao;

import java.util.HashSet;
import java.util.Set;

public class MomentoBuilder {

    private String designation;
    private Float peso;
    private Componente componente;
    private Set<Questao> questoes=new HashSet<>();

    public MomentoBuilder setDesignation(String designation) {
        this.designation = designation;
        return this;
    }

    public MomentoBuilder setPeso(Float peso) {
        this.peso = peso;
        return this;
    }

    public MomentoBuilder setComponente(Componente componente) {
        this.componente = componente;
        return this;
    }

    public MomentoBuilder setQuestoes(Set<Questao> questoes) {
        this.questoes = questoes;
        return this;
    }

    public MomentoBuilder addQuestao(Questao q){
        this.questoes.add(q);
        return this;
    }

    public Momento build(){
        return new Momento(designation,peso, componente, questoes);
    }
}
