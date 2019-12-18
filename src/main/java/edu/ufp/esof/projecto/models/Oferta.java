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
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ano;

    @OneToMany(mappedBy = "oferta",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Componente> componentes=new HashSet<>();

    @OneToMany(mappedBy = "oferta",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<ResultadoAprendizagem> ras=new HashSet<>();




    public Oferta(Integer ano){
        this.setAno(ano);
    }


}