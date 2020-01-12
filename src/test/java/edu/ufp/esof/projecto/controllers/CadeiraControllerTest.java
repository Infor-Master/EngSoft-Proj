package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Criterio;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.CadeiraService;
import edu.ufp.esof.projecto.services.ComponenteService;
import edu.ufp.esof.projecto.services.CriterioService;
import edu.ufp.esof.projecto.services.OfertaService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CadeiraController.class)
class CadeiraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CadeiraService cadeiraService;

    @MockBean
    private ComponenteService componenteService;

    @MockBean
    private CriterioService criterioService;

    @MockBean
    private OfertaService ofertaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCadeiras() throws Exception {
        Cadeira cadeira1=new Cadeira("cadeira1", "111");
        Cadeira cadeira2=new Cadeira("cadeira2", "222");
        Cadeira cadeira3=new Cadeira("cadeira3", "333");
        Set<Cadeira> cadeiras=new HashSet<>();
        cadeiras.add(cadeira1);
        cadeiras.add(cadeira2);
        cadeiras.add(cadeira3);
        when(this.cadeiraService.findAll()).thenReturn(cadeiras);
        String responseJson=this.mockMvc.perform(
                get("/cadeira")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String cadeirajson = jsonObject.toString();
            Cadeira cadeiraresp = this.objectMapper.readValue(cadeirajson, Cadeira.class);

            boolean check=false;
            if(cadeiras.contains(cadeiraresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getCadeiraByName() throws Exception {
        Cadeira cadeira=new Cadeira("Teste", "123");
        when(this.cadeiraService.findByName("Teste")).thenReturn(Optional.of(cadeira));
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        Cadeira responseCadeira=this.objectMapper.readValue(responseJson, Cadeira.class);
        assertEquals(cadeira, responseCadeira);

        this.mockMvc.perform(
                get("/cadeira/Teste2")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    void searchCadeiras() {
    }

    @Test
    void editCadeira() {
    }

    @Test
    void createCadeira() {
    }

    @Test
    void getAllComponentes() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta=new Oferta(2020,cadeira);
        Componente componente1=new Componente("componente1", oferta);
        Componente componente2=new Componente("componente2", oferta);
        Componente componente3=new Componente("componente3", oferta);
        Set<Componente> componentes=new HashSet<>();
        componentes.add(componente1);
        componentes.add(componente2);
        componentes.add(componente3);
        when(this.componenteService.findAll()).thenReturn(componentes);
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/2020/componentes")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String componentejson = jsonObject.toString();
            Componente componenteresp = this.objectMapper.readValue(componentejson, Componente.class);

            boolean check=false;
            if(componentes.contains(componenteresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getComponente() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta=new Oferta(2020,cadeira);
        Componente componente1=new Componente("componente1", oferta);
        Componente componente2=new Componente("componente2", oferta);
        Componente componente3=new Componente("componente3", oferta);
        componente1.setType("TP");
        componente2.setType("PL");
        componente3.setType("PL");
        Set<Componente> componentesPL=new HashSet<>();
        componentesPL.add(componente2);
        componentesPL.add(componente3);
        Set<Componente> componentesTP=new HashSet<>();
        componentesTP.add(componente1);


        when(this.componenteService.findByType("Teste",2020, "PL")).thenReturn(componentesPL);
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/2020/PL")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        when(this.componenteService.findByType("Teste",2020, "TP")).thenReturn(componentesTP);
        responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/2020/TP")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        Cadeira responseCadeira=this.objectMapper.readValue(responseJson, Cadeira.class);
        assertEquals(cadeira, responseCadeira);

        this.mockMvc.perform(
                get("/cadeira/Teste/2019/PL")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    void editComponente() {
    }

    @Test
    void createComponente() {
    }

    @Test
    void getAllCriterios() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Criterio criterio1=new Criterio("Insuficiente",1F);
        Criterio criterio2=new Criterio("Bom", 2F);
        Criterio criterio3=new Criterio("Excelente", 3F);
        Set<Criterio> criterios=new HashSet<>();
        criterios.add(criterio1);
        criterios.add(criterio2);
        criterios.add(criterio3);
        when(this.criterioService.findAll("Teste")).thenReturn(criterios);
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/criterios")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String criteriojson = jsonObject.toString();
            Criterio criterioresp = this.objectMapper.readValue(criteriojson, Criterio.class);

            boolean check=false;
            if(criterios.contains(criterioresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getCriterio() {
    }

    @Test
    void editCriterio() {
    }

    @Test
    void createCriterio() {
    }

    @Test
    void getAllOfertas() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta1=new Oferta(2020,cadeira);
        Oferta oferta2=new Oferta(2019, cadeira);
        Oferta oferta3=new Oferta(2018, cadeira);
        Set<Oferta> ofertas=new HashSet<>();
        ofertas.add(oferta1);
        ofertas.add(oferta2);
        ofertas.add(oferta3);
        when(this.ofertaService.findAll("Teste")).thenReturn(ofertas);
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/ofertas")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String ofertajson = jsonObject.toString();
            Oferta ofertaresp = this.objectMapper.readValue(ofertajson, Oferta.class);

            boolean check=false;
            if(ofertas.contains(ofertaresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getOfertaById() {
    }

    @Test
    void editOferta() {
    }
}