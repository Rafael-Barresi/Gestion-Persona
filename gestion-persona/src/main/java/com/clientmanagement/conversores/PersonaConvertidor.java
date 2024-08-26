package com.clientmanagement.conversores;

import com.clientmanagement.dto.PersonaDTO;
import com.clientmanagement.entidades.Persona;

/**
 *
 * @author Rafael
 */
public class PersonaConvertidor {

    public static Persona aEntidad(PersonaDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Persona(dto.getId(), dto.getNombre(), dto.getEmail(), dto.getCreditos()
                  ,dto.getDireccion(), dto.getGeo(), dto.getGenero(), dto.getPais(), dto.getCreatedAt());
    }

    public static PersonaDTO aEntidad(Persona entidad) {
        if (entidad == null) {
            return null;
        }

        return new PersonaDTO(entidad.getId(), entidad.getNombre(), entidad.getEmail(), entidad.getCreditos()
                  ,entidad.getDireccion(), entidad.getGeo(), entidad.getGenero(), entidad.getPais(), entidad.getCreatedAt());
    }

}
