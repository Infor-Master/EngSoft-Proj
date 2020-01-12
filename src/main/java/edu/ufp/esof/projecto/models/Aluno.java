package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "aluno",cascade = CascadeType.PERSIST)
    //@JsonManagedReference(value = "alunos-momentos")
    //@JsonIgnore
    private Set<MomentoRealizado> momentos=new HashSet<>();

    public Aluno(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }

    public void changeName(String name){
        this.setName(name);
    }

    public void changeCode(String code){
        this.setCode(code);
    }

}