package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.services.AlunoService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlunoController.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAlunos() throws Exception{
        Aluno aluno1=new Aluno("aluno1", "111");
        Aluno aluno2=new Aluno("aluno2", "222");
        Aluno aluno3=new Aluno("aluno3", "333");
        Set<Aluno> alunos=new HashSet<>();
        alunos.add(aluno1);
        alunos.add(aluno2);
        alunos.add(aluno3);
        when(this.alunoService.findAll()).thenReturn(alunos);
        String responseJson=this.mockMvc.perform(
                get("/aluno")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();


        JSONArray jsonArray = new JSONArray(responseJson);
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String alunojson = jsonObject.toString();
            Aluno alunoresp = this.objectMapper.readValue(alunojson, Aluno.class);

            boolean check=false;
            if(alunos.contains(alunoresp)){
                check=true;
            }
            assertTrue(check);
        }
    }

    @Test
    void getAlunoById() throws Exception {
        Aluno aluno=new Aluno("aluno1","123");
        when(this.alunoService.findByNumber("123")).thenReturn(Optional.of(aluno));
        String responseJson=this.mockMvc.perform(
                get("/aluno/123")
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        Aluno responseAluno=this.objectMapper.readValue(responseJson, Aluno.class);
        assertEquals(aluno,responseAluno);

        this.mockMvc.perform(
                get("/aluno/2")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    void editAluno() throws Exception{
        Aluno aluno=new Aluno("alunoOLD", "123");
        Aluno aluno2=new Aluno("alunoNEW", "123");
        when(this.alunoService.updateAluno("123", aluno2)).thenReturn(Optional.of(aluno2));
        String jsonRequest=this.objectMapper.writeValueAsString(aluno2);
        this.mockMvc.perform(
                put("/aluno/123").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                put("/aluno/222").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonRequest)
        ).andExpect(status().isNotFound());

    }

    @Test
    void deleteAllAlunos() {
    }

    @Test
    void deleteAluno() {
    }

    @Test
    void createAluno() throws Exception {
        Aluno aluno=new Aluno("aluno1", "123" );
        when(alunoService.createAluno(aluno)).thenReturn(Optional.of(aluno));
        String jsonRequest=this.objectMapper.writeValueAsString(aluno);

        this.mockMvc.perform(
                post("/aluno").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
        ).andExpect(status().isOk());

        Aluno BADaluno=new Aluno("aluno2", "1234");
        when(this.alunoService.createAluno(BADaluno)).thenReturn(Optional.empty());
        String BADjsonRequest=this.objectMapper.writeValueAsString(BADaluno);

        this.mockMvc.perform(
                post("/aluno").contentType(MediaType.APPLICATION_JSON).content(BADjsonRequest)
        ).andExpect(status().isBadRequest());

    }
}