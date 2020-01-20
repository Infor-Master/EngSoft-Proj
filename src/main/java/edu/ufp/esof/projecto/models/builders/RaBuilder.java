package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.ResultadoAprendizagem;

import java.util.HashSet;
import java.util.Set;

public class RaBuilder {

    private String designation;
    private Oferta oferta;
    private Set<Questao> questoes=new HashSet<>();

    public RaBuilder setDesignation(String designation) {
        this.designation = designation;
        return this;
    }

    public RaBuilder setOferta(Oferta oferta) {
        this.oferta = oferta;
        return this;
    }

    public RaBuilder setQuestoes(Set<Questao> questoes) {
        this.questoes = questoes;
        return this;
    }

    public RaBuilder addQuestao(Questao q){
        this.questoes.add(q);
        return this;
    }

    public ResultadoAprendizagem build(){
        return new ResultadoAprendizagem(designation, oferta, questoes);
    }
}
