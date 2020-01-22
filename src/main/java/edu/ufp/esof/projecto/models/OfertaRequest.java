package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfertaRequest {
    private String cadeiraNome;
    private int ano;
    private Oferta oferta;
}
