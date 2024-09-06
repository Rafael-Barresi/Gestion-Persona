package com.clientmanagement.repositorios;

import com.clientmanagement.entidades.Transaccion;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rafael
 */
@Repository
public interface TransaccionRepositorio extends JpaRepository<Transaccion, String> {

    @Query("SELECT SUM(t.cantidadTransaccion), COUNT(t) "
            + "FROM Transaccion t "
            + "WHERE t.fechaTransaccion BETWEEN :fechaInicio AND :fechaFin")
    public List<Object[]> resultadosTransaccionesSinDTO(
            @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

}
