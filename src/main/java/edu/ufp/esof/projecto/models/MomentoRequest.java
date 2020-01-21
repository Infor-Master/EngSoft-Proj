package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MomentoRequest {
    private String docenteNumero;
    private String cadeiraNome;
    private int ano;
    private String comp;
    private Momento momento;
    private String momentoNome;
}
