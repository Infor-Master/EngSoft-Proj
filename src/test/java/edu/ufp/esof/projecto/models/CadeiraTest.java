package edu.ufp.esof.projecto.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CadeiraTest {

    @Test
    void changeDesignation() {
        Cadeira cadeira=new Cadeira("Old", "123");
        cadeira.changeDesignation("New");
        assertEquals("New", cadeira.getDesignation());
    }

    @Test
    void changeCode() {
        Cadeira cadeira=new Cadeira("Teste", "123");
        cadeira.changeCode("987");
        assertEquals("987", cadeira.getCode());
    }

    @Test
    void addOferta() {
    }

    @Test
    void removeOferta() {
    }
}