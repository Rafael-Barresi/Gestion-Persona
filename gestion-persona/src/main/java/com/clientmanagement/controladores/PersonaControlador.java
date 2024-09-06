package com.clientmanagement.controladores;

import com.clientmanagement.config.LogConfig;
import com.clientmanagement.dto.PersonaDTO;
import com.clientmanagement.servicios.PersonaServicio;
import com.clientmanagement.servicios.TransaccionServicio;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Rafael
 */
@RestController
@RequestMapping("/gestion-personas")
public class PersonaControlador {

    @Autowired
    private PersonaServicio personaServicio;

    @Autowired
    private TransaccionServicio transaccionServicio;

    private static final Logger logger = LogConfig.getLogger(PersonaControlador.class.getName());

    @PostMapping("/sincronizar")
    public ResponseEntity<String> sincronizar(@RequestParam MultipartFile archivo) {
        //valido que el archivo no este vacio
        if (archivo.isEmpty()) {
            return new ResponseEntity<>("El archivo esta vacio", HttpStatus.BAD_REQUEST);
        }

        //valido extension de archivo
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            return new ResponseEntity<>("El Archivo debe ser de tipo .csv", HttpStatus.BAD_REQUEST);
        }

        try {
            //Procesa el archivo csv y obtiene lista de PersonaDTO
            List<PersonaDTO> personas = personaServicio.procesarCSV(archivo);

            //Compara, actualiza sicncroniza con la base de datos
            personaServicio.sincronizarDatos(personas);

            return ResponseEntity.ok("La sincronizacion se realizo con exito!!!");

        } catch (IOException e) {
            //Errores de procesamiento de archivos
            logger.log(Level.SEVERE, "Error al procesar el archivo csv: {0} Detalle: {1}",
                    new Object[]{nombreArchivo, e.getMessage()});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR al procesar archivo.csv: " + e.getMessage());

        } catch (Exception e) {
            //errores inesperado
            logger.log(Level.SEVERE, "ERROR inesperado: {0}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR inesperado: " + e.getMessage());
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> transferencia(
            @RequestParam String idEmisor,
            @RequestParam Integer creditos,
            @RequestParam String idReceptor) {

        if (idEmisor == null || idEmisor.isEmpty()) {
            return ResponseEntity.badRequest().body("ERROR el id del emisor no puede estar vacio.");
        }
        if (idReceptor == null || idReceptor.isEmpty()) {
            return ResponseEntity.badRequest().body("ERROR el id del receptor no puede estar vacio.");
        }
        if (creditos == null || creditos <= 0) {
            return ResponseEntity.badRequest().body("ERROR la cantidad de creditos debe ser mayor a 0.");
        }
        if (idEmisor.equals(idReceptor)) {
            return ResponseEntity.badRequest().body("ERROR el emisor y el receptor no pueden ser iguales");
        }
        try {
            personaServicio.transferenciaCreditos(idEmisor, creditos, idReceptor);
            return ResponseEntity.ok("Transferencia realizada con exito!!!.");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "ERROR en la solicitud: {0}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "ERROR en la base de datos: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR interno del servidor a; acceder a la base de datos.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "ERROR inesperado {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrio error inesperado");
        }
    }

    @GetMapping("/informe-transferencias")
    public ResponseEntity<Map<String, Long>> informeTransferencias(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaFin) {

        Map<String, Long> respuestaError = new HashMap<>();
        if (fechaInicio == null || fechaFin == null) {
            respuestaError.put("ERROR: las fechas no pueden ser nulas", -1L);
            return ResponseEntity.badRequest().body(respuestaError);
        }

        if (fechaInicio.isAfter(fechaFin)) {
            respuestaError.put("ERROR la fecha de inicio no puede ser posterior  a la fecha de fin", -2L);
            return ResponseEntity.badRequest().body(respuestaError);
        }

        try {

            Map<String, Long> informe = transaccionServicio.transaccionEntreFechas(fechaInicio, fechaFin);

            if (informe.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 NoContent si no hay trransacciones.
            }
            return ResponseEntity.ok(informe); // 200 ok con el informe
        } catch (Exception e) {
            respuestaError.put("ERROR inesperado", -3L);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);

        }

    }

}
