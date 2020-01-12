package edu.ufp.esof.projecto.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponenteTest {

    @Test
    void changeType() {
        Cadeira cadeira=new Cadeira("Teste", "123");
        Oferta oferta = new Oferta(2020, cadeira);
        Componente componente=new Componente("Old", oferta);
        componente.changeType("New");
        assertEquals("New", componente.getType());
    }

    @Test
    void addDocente() {
        Cadeira cadeira=new Cadeira("Teste", "123");
        Oferta oferta = new Oferta(2020, cadeira);
        Componente componente=new Componente("Old", oferta);
        Docente docente=new Docente("docente", "001");
        componente.addDocente(docente);
        assertEquals(docente, componente.getDocente());
    }

    @Test
    void removeDocente() {
        Cadeira cadeira=new Cadeira("Teste", "123");
        Oferta oferta = new Oferta(2020, cadeira);
        Componente componente=new Componente("Old", oferta);
        Docente docente=new Docente("docente", "001");
        componente.addDocente(docente);
        componente.removeDocente();
        assertNull(componente.getDocente());
    }

    @Test
    void addAluno() {
        Cadeira cadeira=new Cadeira("Teste", "123");
        Oferta oferta = new Oferta(2020, cadeira);
        Componente componente=new Componente("Old", oferta);
        Aluno aluno=new Aluno("estudante", "001");
        componente.addAluno(aluno);
        assertTrue(componente.getAlunos().contains(aluno));
        assertEquals(1, componente.getAlunos().size());
    }

    @Test
    void removeAluno() {
        Cadeira cadeira=new Cadeira("Teste", "123");
        Oferta oferta = new Oferta(2020, cadeira);
        Componente componente=new Componente("Old", oferta);
        Aluno aluno=new Aluno("estudante", "001");
        aluno.setId(1L);
        componente.addAluno(aluno);
        componente.removeAluno(aluno.getId());
        assertEquals(0, componente.getAlunos().size());
    }
}