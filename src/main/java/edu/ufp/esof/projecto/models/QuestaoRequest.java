package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestaoRequest {
    private String docenteNumero;
    private String cadeiraNome;
    private int ano;
    private String comp;
    private String momentoNome;
    private String questaoNome;
    private Questao questao;
    private String raNome;
}
