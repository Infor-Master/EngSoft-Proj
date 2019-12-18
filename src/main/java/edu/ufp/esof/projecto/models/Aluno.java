package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    @ManyToMany(mappedBy = "alunos", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Componente> componentes=new HashSet<>();

    @OneToMany(mappedBy = "aluno",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<MomentoRealizado> momentos=new HashSet<>();

    public Aluno(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }

}