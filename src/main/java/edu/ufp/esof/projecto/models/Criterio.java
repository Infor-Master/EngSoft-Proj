package edu.ufp.esof.projecto.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Criterio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private Float peso;

    public Criterio(String designation, Float peso){
        this.setDesignation(designation);
        this.setPeso(peso);
    }
}
