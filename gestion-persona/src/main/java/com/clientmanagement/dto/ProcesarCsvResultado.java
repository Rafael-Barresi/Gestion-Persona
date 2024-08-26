
package com.clientmanagement.dto;

import java.util.List;

/**
 *
 * @author Rafael
 */
public class ProcesarCsvResultado {
    
    private List<PersonaDTO> personasValidas;
    private Integer registrosInvalidos;
    
    public ProcesarCsvResultado () {
        
    }

    public List<PersonaDTO> getPersonasValidas() {
        return personasValidas;
    }

    public void setPersonasValidas(List<PersonaDTO> personasValidas) {
        this.personasValidas = personasValidas;
    }

    public Integer getRegistrosInvalidos() {
        return registrosInvalidos;
    }

    public void setRegistrosInvalidos(Integer registrosInvalidos) {
        this.registrosInvalidos = registrosInvalidos;
    }
    
}
