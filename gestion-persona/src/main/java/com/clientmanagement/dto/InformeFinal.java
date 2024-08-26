
package com.clientmanagement.dto;

import java.util.Map;

/**
 *
 * @author Rafael
 */
public class InformeFinal {
    
    private Integer invalidos;
    private Map<String, Integer>informeGestionPersona;
    
    public InformeFinal() {
        
    }

    public Integer getInvalidos() {
        return invalidos;
    }

    public void setInvalidos(Integer invalidos) {
        this.invalidos = invalidos;
    }

    public Map<String, Integer> getInformeGestionPersona() {
        return informeGestionPersona;
    }

    public void setInformeGestionPersona(Map<String, Integer> informeGestionPersona) {
        this.informeGestionPersona = informeGestionPersona;
    }
    
    
}
