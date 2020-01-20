package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.*;

import java.util.HashSet;
import java.util.Set;

public class ComponenteBuilder {

    private String type;
    private Docente docente = null;
    private Set<Aluno> alunos=new HashSet<>();
    private Oferta oferta;
    private Set<Momento> momentos=new HashSet<>();

    public ComponenteBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public ComponenteBuilder setDocente(Docente docente) {
        this.docente = docente;
        return this;
    }

    public ComponenteBuilder setAlunos(Set<Aluno> alunos) {
        this.alunos = alunos;
        return this;
    }

    public ComponenteBuilder addAluno(Aluno a){
        this.alunos.add(a);
        return this;
    }

    public ComponenteBuilder setOferta(Oferta oferta) {
        this.oferta = oferta;
        return this;
    }

    public ComponenteBuilder setMomentos(Set<Momento> momentos) {
        this.momentos = momentos;
        return this;
    }

    public ComponenteBuilder addMomentos(Momento m){
        this.momentos.add(m);
        return this;
    }

    public Componente build(){
        return new Componente(type, docente, alunos, oferta, momentos);
    }
}
