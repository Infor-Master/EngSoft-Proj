package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Escala;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.CadeiraService;
import edu.ufp.esof.projecto.services.ComponenteService;
import edu.ufp.esof.projecto.services.EscalaService;
import edu.ufp.esof.projecto.services.OfertaService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
    private EscalaService escalaService;

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
    void searchCadeiras() throws Exception {
        Cadeira cadeira1=new Cadeira("cadeira1", "111");
        Set<Cadeira> cadeiras=new HashSet<>();
        cadeiras.add(cadeira1);
        Map<String, String> Query = new HashMap<String, String>();
        Query.put("codigo", "111");
        when(this.cadeiraService.filterCadeira(Query)).thenReturn(cadeiras);
        String responseJson=this.mockMvc.perform(
                get("/cadeira/search?codigo=111")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String cadeirajson = jsonObject.toString();
            Cadeira cadeiraresp = this.objectMapper.readValue(cadeirajson, Cadeira.class);
            assertEquals(cadeiraresp, cadeira1);
        }
        assertNotEquals(jsonArray.length(), 0);

        String BADresponseJson=this.mockMvc.perform(
                get("/cadeira/search?codigo=222")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray BADjsonArray = new JSONArray(BADresponseJson);
        assertEquals(BADjsonArray.length(), 0);
    }

    @Test
    void editCadeira() throws Exception {
        Cadeira cadeira=new Cadeira("cadeira1", "123");
        Cadeira cadeira2=new Cadeira("cadeira1", "222");
        when(this.cadeiraService.updateCadeira("cadeira1", cadeira2)).thenReturn(Optional.of(cadeira2));
        String jsonRequest=this.objectMapper.writeValueAsString(cadeira2);
        this.mockMvc.perform(
                put("/cadeira/cadeira1").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                put("/cadeira/cadeiraWRONG").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isNotFound());
    }

    @Test
    void createCadeira() throws Exception{
        Cadeira cadeira=new Cadeira("Teste", "123");
        when(cadeiraService.createCadeira(cadeira)).thenReturn(Optional.of(cadeira));
        String jsonRequest=this.objectMapper.writeValueAsString(cadeira);

        this.mockMvc.perform(
                post("/cadeira").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().isOk());

        Cadeira BADcadeira=new Cadeira("TesteBAD", "321");
        when(this.cadeiraService.createCadeira(BADcadeira)).thenReturn(Optional.empty());
        String BADjsonRequest=this.objectMapper.writeValueAsString(BADcadeira);

        this.mockMvc.perform(
                post("/cadeira").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());
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
        when(this.componenteService.findAll("Teste", 2020)).thenReturn(componentes);
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
        componente1.setType("TP");
        componente2.setType("PL");


        when(this.componenteService.findByType("Teste",2020, "PL")).thenReturn(Optional.of(componente1));
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/2020/PL")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        when(this.componenteService.findByType("Teste",2020, "TP")).thenReturn(Optional.of(componente2));
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
    void editComponente() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta=new Oferta(2020,cadeira);
        Componente componente=new Componente("PL", oferta);
        Componente componente2=new Componente("TP", oferta);
        when(this.componenteService.updateComponente("Teste",2020,"PL", componente2)).thenReturn(Optional.of(componente2));
        String jsonRequest=this.objectMapper.writeValueAsString(componente2);
        this.mockMvc.perform(
                put("/cadeira/Teste/2020/PL").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                put("/cadeira/Teste/2020/LAB").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isNotFound());
    }

    @Test
    void createComponente() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta=new Oferta(2020, cadeira);
        Componente componente=new Componente("PL", oferta);
        when(componenteService.createComponente("Teste",2020, componente)).thenReturn(Optional.of(componente));
        String jsonRequest=this.objectMapper.writeValueAsString(componente);

        this.mockMvc.perform(
                post("/cadeira/Teste/2020").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().isOk());

        Componente BADcomponente=new Componente("TP", oferta);
        when(componenteService.createComponente("Teste", 2020, BADcomponente)).thenReturn(Optional.empty());
        String BADjsonRequest=this.objectMapper.writeValueAsString(BADcomponente);

        this.mockMvc.perform(
                post("/cadeira/Teste/2020").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getAllCriterios() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Escala escala1 =new Escala("Insuficiente",1F);
        Escala escala2 =new Escala("Bom", 2F);
        Escala escala3 =new Escala("Excelente", 3F);
        Set<Escala> escalas =new HashSet<>();
        escalas.add(escala1);
        escalas.add(escala2);
        escalas.add(escala3);
        when(this.escalaService.findAll("Teste")).thenReturn(escalas);
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/escalas")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String criteriojson = jsonObject.toString();
            Escala criterioresp = this.objectMapper.readValue(criteriojson, Escala.class);

            boolean check=false;
            if(escalas.contains(criterioresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getCriterio() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Escala escala =new Escala("bom", 10F);
        when(this.escalaService.findByDesignation("Teste", "bom")).thenReturn(Optional.of(escala));
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/escalas/bom")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        this.mockMvc.perform(
                get("/cadeira/Teste/escalas/mau")
        ).andExpect(
                status().isNotFound()
        );
    }
/**
    @Test
    void editCriterio() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Escala escala =new Escala("bom", 10F);
        Escala escala2 =new Escala("bom", 15F);

        when(this.escalaService.updateEscala("Teste","bom", escala2)).thenReturn(Optional.of(escala2));
        String jsonRequest=this.objectMapper.writeValueAsString(escala2);
        this.mockMvc.perform(
                put("/cadeira/Teste/escalas/bom").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                put("/cadeira/Teste/escalas/mau").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isNotFound());
    }
**/
    @Test
    void createCriterio() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Escala escala =new Escala("bom", 10F);
        when(escalaService.createEscala("Teste", escala)).thenReturn(Optional.of(escala));
        String jsonRequest=this.objectMapper.writeValueAsString(escala);

        this.mockMvc.perform(
                post("/cadeira/Teste/escalas").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().isOk());

        Escala BADcriterio=new Escala("bom", 99F);
        when(escalaService.createEscala("Teste", BADcriterio)).thenReturn(Optional.empty());
        String BADjsonRequest=this.objectMapper.writeValueAsString(BADcriterio);

        this.mockMvc.perform(
                post("/cadeira/Teste/escalas").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());
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
    void getOfertaById() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta=new Oferta(2020,cadeira);
        when(this.ofertaService.find("Teste", 2020)).thenReturn(Optional.of(oferta));
        String responseJson=this.mockMvc.perform(
                get("/cadeira/Teste/2020")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        this.mockMvc.perform(
                get("/cadeira/Teste/2099")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    void editOferta() throws Exception {
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta1=new Oferta(2020,cadeira);
        Oferta oferta2=new Oferta(2019,cadeira);

        when(this.ofertaService.updateOferta("Teste",2020, oferta2)).thenReturn(Optional.of(oferta2));
        String jsonRequest=this.objectMapper.writeValueAsString(oferta2);
        this.mockMvc.perform(
                put("/cadeira/Teste/2020").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                put("/cadeira/Teste/2099").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isNotFound());
    }

    @Test
    void createOferta() throws Exception{
        Cadeira cadeira=new Cadeira("Teste","123");
        Oferta oferta=new Oferta(2020, cadeira);
        when(ofertaService.createOferta("Teste", oferta)).thenReturn(Optional.of(oferta));
        String jsonRequest=this.objectMapper.writeValueAsString(oferta);

        this.mockMvc.perform(
                post("/cadeira/Teste/ofertas").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().isOk());

        Oferta BADoferta=new Oferta(2099, cadeira);
        when(ofertaService.createOferta("Teste", BADoferta)).thenReturn(Optional.empty());
        String BADjsonRequest=this.objectMapper.writeValueAsString(BADoferta);

        this.mockMvc.perform(
                post("/cadeira/Teste/ofertas").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());
    }
}