
package com.clientmanagement.repositorios;

import com.clientmanagement.entidades.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rafael
 */
@Repository
public interface PersonaRepositorio extends JpaRepository<Persona, String> {
    
}
