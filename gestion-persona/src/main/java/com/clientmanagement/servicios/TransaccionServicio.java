package com.clientmanagement.servicios;

import com.clientmanagement.config.LogConfig;
import com.clientmanagement.entidades.Transaccion;
import com.clientmanagement.repositorios.TransaccionRepositorio;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rafael
 */
@Service
public class TransaccionServicio {

    @Autowired
    private TransaccionRepositorio transaccionRepositorio;

    private final static Logger logger = LogConfig.getLogger(TransaccionServicio.class.getName());

 /**
 *Crea una nueva transaccion y la persiste. 
 * @param creditos
 * @throws Exception 
 */
    @Transactional
    public void transaccionNueva(Integer creditos) throws Exception {

        if (creditos == null) {
            throw new IllegalArgumentException("ERROR el valor credito esta vacio.");
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setCantidadTransaccion(creditos);

        try {
            transaccionRepositorio.save(transaccion);

        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "ERROR al guardar la transaccion en la base de datos.", e);
            throw new RuntimeException("ERROR al  persistr la transaccion", e);
        }
    }

    /**
     * devuelve datos de transacciones realizadas en un periodo especifico
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public Map<String, Long> transaccionEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        Map<String, Long> informe = new HashMap<>();

        if (fechaInicio == null || fechaFin == null) {
            logger.log(Level.WARNING, "Las fechas de inicio y fin no pueden ser nulas");
            throw new IllegalArgumentException("ERROR: Las fechas de inicio y fin no pueden ser nula.");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            logger.log(Level.WARNING, "La fecha de inicio no puede ser posterior a la fecha de fin.");
            throw new IllegalArgumentException("ERROR: La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
         try {
             
        List<Object[]> resultados = transaccionRepositorio.resultadosTransaccionesSinDTO(fechaInicio, fechaFin);
       
        if (resultados != null && !resultados.isEmpty()) {
            
            Object[] resultado = resultados.get(0); // Debería haber solo un resultado
            Long suma = (Long) resultado[0];
            Long transacciones = (Long) resultado[1];
            
            informe.put("Cantidad de creditos trandferidos entre fechas: ", suma);
            informe.put("Cantidad de transferencias realizadas entre fechas: ", transacciones);
       
        } else {
            logger.log(Level.WARNING, "No se encontraron resultados.");
        }
            } catch (ClassCastException e) {
                logger.log(Level.SEVERE, "Error en el casting de resultados de transacciones.", e);
                informe.put("Cantidad de creditos transferidos entre fechas: ", 0L);
                informe.put("Cantidad de transferencias realizadas entre fechas: ", 0L);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR inesperado: ", e);
            }
        
        return informe;
    }

}
