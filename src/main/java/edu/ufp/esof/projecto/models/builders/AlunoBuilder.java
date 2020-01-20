package edu.ufp.esof.projecto.models.builders;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Momento;
import edu.ufp.esof.projecto.models.MomentoRealizado;

import java.util.HashSet;
import java.util.Set;

public class AlunoBuilder {

    private String name;
    private String code;
    private Set<Componente> componentes=new HashSet<>();
    private Set<MomentoRealizado> momentos=new HashSet<>();

    public AlunoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AlunoBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public AlunoBuilder setComponentes(Set<Componente> componentes) {
        this.componentes = componentes;
        return this;
    }

    public AlunoBuilder addComponentes(Componente componente){
        this.componentes.add(componente);
        return this;
    }

    public AlunoBuilder setMomentos(Set<MomentoRealizado> momentos) {
        this.momentos = momentos;
        return this;
    }

    public AlunoBuilder addMomentos(MomentoRealizado momentoRealizado){
        this.momentos.add(momentoRealizado);
        return this;
    }

    public Aluno build(){
        return new Aluno(name,code,componentes,momentos);
    }
}
