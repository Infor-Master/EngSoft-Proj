package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RARequest {
    private String cadeiraNome;
    private int ano;
    private ResultadoAprendizagem resultadoAprendizagem;
    private String RANome;
}
