package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Oferta;
import edu.ufp.esof.projecto.services.ComponenteService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ComponenteController.class)
class ComponenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComponenteService componenteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchComponentes() throws Exception{
        Cadeira cadeira=new Cadeira("Teste", "123");
        Oferta oferta=new Oferta(2020, cadeira);
        Componente componente1=new Componente("componente1", oferta);
        Set<Componente> componentes=new HashSet<>();
        componentes.add(componente1);
        Map<String, String> Query = new HashMap<String, String>();
        Query.put("ano", "2020");
        when(this.componenteService.filterComponente(Query)).thenReturn(componentes);
        String responseJson=this.mockMvc.perform(
                get("/componente/search?ano=2020")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String componentejson = jsonObject.toString();
            Componente componenteresp = this.objectMapper.readValue(componentejson, Componente.class);
            assertEquals(componenteresp, componente1);
        }
        assertNotEquals(jsonArray.length(), 0);

        String BADresponseJson=this.mockMvc.perform(
                get("/componente/search?ano=2099")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        JSONArray BADjsonArray = new JSONArray(BADresponseJson);
        assertEquals(BADjsonArray.length(), 0);
    }
}