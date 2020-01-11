package edu.ufp.esof.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.services.AlunoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void getAllAlunos() {
    }

    @Test
    void getAlunoById() {
    }

    @Test
    void editAluno() {
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