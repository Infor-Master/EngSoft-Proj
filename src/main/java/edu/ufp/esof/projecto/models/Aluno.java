package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIdentityInfo(
        generator= ObjectIdGenerators.PropertyGenerator.class,
        property="id")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
@NoArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String code;

    @ManyToMany(mappedBy = "alunos", cascade = CascadeType.PERSIST)
    //@JsonManagedReference(value = "alunos-componentes")
    @JsonIgnore     // perguntar ao professor
    @ToString.Exclude   // perguntar ao professor
    @EqualsAndHashCode.Exclude  // perguntar ao professor
    private Set<Componente> componentes=new HashSet<>();

    @OneToMany(mappedBy = "aluno",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    //@JsonManagedReference(value = "alunos-momentos")
    //@JsonIgnore
    private Set<MomentoRealizado> momentos=new HashSet<>();

    public Aluno(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }

    public Aluno(String name, String code, Set<Componente> componentes, Set<MomentoRealizado> momentos) {
        this.name = name;
        this.code = code;
        this.componentes = componentes;
        for (Componente c : componentes) {
            c.getAlunos().add(this);
        }
        this.momentos = momentos;
        for (MomentoRealizado m : momentos) {
            m.setAluno(this);
        }
    }

    public void changeName(String name){
        this.setName(name);
    }

    public void changeCode(String code){
        this.setCode(code);
    }

    public void inscreverNaComponente(Componente c){
        if (!componentes.contains(c) && !c.getAlunos().contains(this)){
            c.getAlunos().add(this);
            componentes.add(c);
        }
    }

    public void desinscreverDaComponente(Componente c){
        if (componentes.contains(c) && c.getAlunos().contains(this)){
            c.getAlunos().remove(this);
            componentes.remove(c);
        }
    }
}