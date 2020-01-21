package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.models.builders.ComponenteBuilder;
import edu.ufp.esof.projecto.models.builders.MomentoBuilder;
import edu.ufp.esof.projecto.services.DocenteService;
import edu.ufp.esof.projecto.services.MomentoService;
import edu.ufp.esof.projecto.services.QuestaoService;
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

@WebMvcTest(controllers = DocenteController.class)
class DocenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocenteService docenteService;

    @MockBean
    private MomentoService momentoService;

    @MockBean
    private QuestaoService questaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchDocentes() throws Exception {
        Docente docente1=new Docente("docente1", "111");
        Set<Docente> docentes=new HashSet<>();
        docentes.add(docente1);
        Map<String, String> Query = new HashMap<String, String>();
        Query.put("codigo", "111");
        when(this.docenteService.filterDocente(Query)).thenReturn(docentes);
        String responseJson=this.mockMvc.perform(
                get("/docente/search?codigo=111")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String docentejson = jsonObject.toString();
            Docente docenteresp = this.objectMapper.readValue(docentejson, Docente.class);
            assertEquals(docenteresp, docente1);
        }
        assertNotEquals(jsonArray.length(), 0);

        String BADresponseJson=this.mockMvc.perform(
                get("/docente/search?codigo=222")
        ).andExpect(status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray BADjsonArray = new JSONArray(BADresponseJson);
        assertEquals(BADjsonArray.length(), 0);
    }

    @Test
    void getAllDocentes() throws Exception {
        Docente docente1=new Docente("docente1", "111");
        Docente docente2=new Docente("docente2", "222");
        Docente docente3=new Docente("docente3", "333");
        Set<Docente> docentes=new HashSet<>();
        docentes.add(docente1);
        docentes.add(docente2);
        docentes.add(docente3);
        when(this.docenteService.findAll()).thenReturn(docentes);
        String responseJson=this.mockMvc.perform(
                get("/docente")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String docentejson = jsonObject.toString();
            Docente docenteresp = this.objectMapper.readValue(docentejson, Docente.class);

            boolean check=false;
            if(docentes.contains(docenteresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getDocenteById() throws Exception {
        Docente docente=new Docente("Teste", "123");
        when(this.docenteService.findByNumber("123")).thenReturn(Optional.of(docente));
        String responseJson=this.mockMvc.perform(
                get("/docente/123")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        Docente responseDocente=this.objectMapper.readValue(responseJson, Docente.class);
        assertEquals(docente, responseDocente);

        this.mockMvc.perform(
                get("/docente/987")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    void editDocente() throws Exception {
        Docente docente=new Docente("docente1", "123");
        Docente docente2=new Docente("docente1", "222");
        when(this.docenteService.updateDocente("123", docente2)).thenReturn(Optional.of(docente2));
        String jsonRequest=this.objectMapper.writeValueAsString(docente2);
        this.mockMvc.perform(
                put("/docente/123").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                put("/docente/987").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isNotFound());
    }

    @Test
    void createDocente() throws Exception {
        Docente docente=new Docente("docente", "123");
        when(docenteService.createDocente(docente)).thenReturn(Optional.of(docente));
        String jsonRequest=this.objectMapper.writeValueAsString(docente);

        this.mockMvc.perform(
                post("/docente").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().isOk());

        Docente BADdocente=new Docente("docenteBAD", "321");
        when(this.docenteService.createDocente(BADdocente)).thenReturn(Optional.empty());
        String BADjsonRequest=this.objectMapper.writeValueAsString(BADdocente);

        this.mockMvc.perform(
                post("/docente").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deleteAllDocentes() throws Exception {
        this.mockMvc.perform(
                delete("/docente")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteDocente() throws Exception {
        Docente docente = new Docente("docente", "123");
        when(this.docenteService.deleteDocente("123")).thenReturn(true);
        this.mockMvc.perform(
                delete("/docente/123").contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                delete("/docente/1234").contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    void associateDocenteComponente() {
    }

    @Test
    void desassociateDocenteComponente() {
    }

    @Test
    void createMomento() throws Exception {
        Cadeira cadeira = new Cadeira("Engenharia de Software", "111");
        Oferta oferta= new Oferta(2020, cadeira);
        Docente docente=new Docente("docente", "123");
        Componente componente = new ComponenteBuilder().setDocente(docente).setOferta(oferta).setType("pratica").build();
        Momento momento = new MomentoBuilder().setComponente(componente).setPeso(0.2F).setDesignation("TesteR").build();

        when(momentoService.createMomento(docente.getCode(), cadeira.getDesignation(), oferta.getAno(), componente.getType(), momento)).thenReturn(Optional.of(momento));
        String jsonRequest="{\"docenteNumero\": \"123\",\"cadeiraNome\": \"Engenharia de Software\",\"ano\": 2020,\"comp\": \"pratica\",\"momento\": {\"designation\": \"testeR\", \"peso\": 0.2}}";

        this.mockMvc.perform(
                post("/docente/momento").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().is(400));

        when(momentoService.createMomento(docente.getCode(), cadeira.getDesignation(), oferta.getAno(), componente.getType(), momento)).thenReturn(Optional.empty());
        String BADjsonRequest= "{\"docenteNumero\": \"123\",\"cadeiraNome\": \"Engenharia de Software\",\"ano\": 2020,\"comp\": \"pratica\",\"momento\": {\"designation\": \"testeR\", \"peso\": 0.2}}";
        this.mockMvc.perform(
                post("/docente/momento").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deleteMomento() {
    }

    @Test
    void createQuest() {
    }

    @Test
    void deleteQuestao() {
    }
}