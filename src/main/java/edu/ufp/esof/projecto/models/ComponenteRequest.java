package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponenteRequest {
    private String cadeiraNome;
    private int ano;
    private String type;
    private Componente componente;
}
