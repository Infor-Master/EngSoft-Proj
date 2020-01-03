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
public class Cadeira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private String code;

    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Oferta> ofertas = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Criterio> criterios = new HashSet<>();

    public Cadeira(String designation, String code){
        this.setDesignation(designation);
        this.setCode(code);
    }

    /**
     * Atualiza a designação da cadeira
     * @param designation Nova designação
     */
    public void changeDesignation(String designation){
        setDesignation(designation);
    }

    /**
     * Atualiza o código da cadeira
     * @param code Novo código
     */
    public void changeCode(String code){
        setCode(code);
    }

    /**
     * Adiciona uma oferta à cadeira
     * @param offer Oferta a adicionar
     */
    public void addOferta(Oferta offer){
        if (addCheckOferta(offer))
            ofertas.add(offer);
    }

    /**
     * Remove uma oferta da cadeira
     * @param id Id da oferta a remover
     * @return Cadeira removida
     */
    public Oferta removeOferta(int id){
        for (Oferta o : ofertas) {
            if (o.getId().equals(id)){
                ofertas.remove(o);
                return o;
            }
        }
        return null;
    }

    /**
     * Verifica se oferta pertence à disciplina
     * @param offer Oferta a verificar
     * @return True se pertence, false se não
     */
    private boolean offerBelongsToDiscipline(Oferta offer){
        return offer.getCadeira().getId().equals(id);
    }

    /**
     * Verifica a validade da oferta para ser inserida na cadeira
     * @param offer Oferta a verificar
     * @return True se válida, False se não
     */
    private boolean addCheckOferta(Oferta offer){
        if (offerBelongsToDiscipline(offer)){
            for (Oferta o : ofertas) {
                if(o.getAno().equals(offer.getAno()))
                    return false;
            }
        }
        return true;
    }
}
