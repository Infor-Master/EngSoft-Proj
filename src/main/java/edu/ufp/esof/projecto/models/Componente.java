package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.*;
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
@NoArgsConstructor
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //@JsonBackReference
    private Docente docente = null;


    @ManyToMany
    //@JsonBackReference(value = "alunos-componentes")
    @ToString.Exclude   // perguntar ao professor
    @EqualsAndHashCode.Exclude  // perguntar ao professor
    private Set<Aluno> alunos=new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    //@JsonBackReference
    private Oferta oferta;

    @OneToMany(mappedBy = "componente",cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Momento> momentos=new HashSet<>();

    public Componente(String type, Oferta oferta) {
        this.type = type;
        this.oferta = oferta;
        oferta.getComponentes().add(this);
    }

    public Componente(String type, Docente docente, Set<Aluno> alunos, Oferta oferta, Set<Momento> momentos) {
        this.type = type;
        this.docente = docente;
        docente.getComponentes().add(this);
        this.alunos = alunos;
        for (Aluno a : alunos) {
            a.getComponentes().add(this);
        }
        this.oferta = oferta;
        oferta.getComponentes().add(this);
        this.momentos = momentos;
    }

    public void changeType(String type){
        setType(type);
    }

    public void addDocente(Docente d){
        if (docente == null){
            docente = d;
            d.getComponentes().add(this);
        }
    }

    public Docente removeDocente(){
        Docente d = docente;
        if (docente != null){
            //docente.removeComponente(this);
            docente = null;
        }
        return d;
    }

    public void addAluno(Aluno a){
        if(!alunos.contains(a)){
            alunos.add(a);
            //a.addComponente(a);
        }
    }

    public Aluno removeAluno(long id){
        for (Aluno a : alunos) {
            if (a.getId().equals(id)){
                a.getComponentes().remove(this);
                alunos.remove(a);
                return a;
            }
        }
        return null;
    }
}