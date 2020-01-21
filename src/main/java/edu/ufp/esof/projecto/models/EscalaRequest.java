package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EscalaRequest {
    private String cadeiraNome;
    private Escala escala;
    private String designation;
}
