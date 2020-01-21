package edu.ufp.esof.projecto.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriterioRequest {
    private String cadeiraNome;
    private Criterio criterio;
}
