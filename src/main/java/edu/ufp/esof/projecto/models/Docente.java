package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIdentityInfo(
        generator= ObjectIdGenerators.PropertyGenerator.class,
        property="id")
@NoArgsConstructor
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    //@OneToMany(mappedBy = "docente",cascade = CascadeType.PERSIST)
    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Componente> componentes=new HashSet<>();


    public Docente(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }

    public void associateDocenteComponente(Componente comp){
        for (Componente c : componentes) {
            if (c.getId() == comp.getId()){
                return;
            }
        }
        comp.setDocente(this);
        componentes.add(comp);
    }

    public void desassociateDocenteComponente(Componente comp){
        for (Componente c : componentes) {
            if (c.getId() == comp.getId()){
                comp.setDocente(null);
                componentes.remove(comp);
                return;
            }
        }
    }
}