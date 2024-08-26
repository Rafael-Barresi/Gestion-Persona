package com.clientmanagement.controladores;

import com.clientmanagement.config.LogConfig;
import com.clientmanagement.dto.PersonaDTO;
import com.clientmanagement.servicios.CrmApiServicio;
import com.clientmanagement.servicios.PersonaServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     

    @PostMapping("/json")
    public ResponseEntity<?> procesarPersona(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        // Log del JSON recibido
        logger.info("JSON recibido: " + json);
        try {
            // Deserialización
            PersonaDTO persona = mapper.readValue(json, PersonaDTO.class);
            // Procesar la persona
            return ResponseEntity.ok(persona);
        } catch (Exception e) {
            // Log de errores
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar JSON");
        }
    }

}
