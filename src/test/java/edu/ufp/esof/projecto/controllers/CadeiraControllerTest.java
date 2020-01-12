package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.Cadeira;
import edu.ufp.esof.projecto.services.CadeiraService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
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
    void getCadeiraByName() {
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
    void testGetAllCadeiras() {
    }

    @Test
    void testGetCadeiraByName() {
    }

    @Test
    void testSearchCadeiras() {
    }

    @Test
    void testEditCadeira() {
    }

    @Test
    void testCreateCadeira() {
    }

    @Test
    void getAllComponentes() {
    }

    @Test
    void getComponente() {
    }

    @Test
    void editComponente() {
    }

    @Test
    void createComponente() {
    }

    @Test
    void getAllCriterios() {
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
    void getAllOfertas() {
    }

    @Test
    void getOfertaById() {
    }

    @Test
    void editOferta() {
    }
}