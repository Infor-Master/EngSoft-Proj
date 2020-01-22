package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotaRequest {
    private String docenteNumero;
    private String alunoNumero;
    private int ano;
    private String cadeiraNome;
    private String componente;
    private String momentoNome;
    private String questaoNome;
    private String escalaNome;
}
