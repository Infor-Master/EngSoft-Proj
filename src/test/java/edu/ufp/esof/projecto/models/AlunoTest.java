package edu.ufp.esof.projecto.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void changeName() {
        Aluno aluno=new Aluno("Old", "123");
        aluno.changeName("New");
        assertEquals("New", aluno.getName());
    }

    @Test
    void changeCode() {
        Aluno aluno=new Aluno("teste", "123");
        aluno.changeCode("987");
        assertEquals("987", aluno.getCode());
    }
}