package com.clientmanagement.servicios;

import com.clientmanagement.config.LogConfig;
import com.clientmanagement.conversores.PersonaConvertidor;
import com.clientmanagement.dto.PersonaDTO;
import com.clientmanagement.entidades.Persona;
import com.clientmanagement.repositorios.PersonaRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Rafael
 */
@Service
public class PersonaServicio {

    @Autowired
    private CrmApiServicio crmApiServicio;
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private TransaccionServicio transaccionServicio;
    //private static final Logger logger = Logger.getLogger(PersonaServicio.class.getName());
    private final static Logger logger = LogConfig.getLogger(PersonaServicio.class.getName());

    @Autowired
    private ObjectMapper mapper;

    /**
     * procesa archivo csv y devuelve lista de PersonaDTO valida cada campo del
     * archivo
     *
     * @param file
     * @return List<PersonaDTO>
     * @throws IOException
     */
    public List<PersonaDTO> procesarCSV(MultipartFile archivo) throws IOException {

        //Creo archivo temporal en el sistema
        File archivoTemporal = File.createTempFile("temporal", ".csv");
        //Transfiero lo que viene por parametro al archivo temporal
        archivo.transferTo(archivoTemporal);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoTemporal))) {

            String encabezadosLinea = bufferedReader.readLine();
            List<String> encabezadosReales = Arrays.asList(encabezadosLinea.split(","));

            List<String> encabezadoValido = Arrays.asList(
                    "id", "nombre", "email", "creditos", "direccion",
                    "geoLat", "geoLong", "genero", "pais", "createdAt"
            );
            if (!encabezadosReales.equals(encabezadoValido)) {
                throw new IOException("ERROR: Los encabezados del archivo no son los esperados!!!!");
            }

            ///Validacion de campos csv
            String linea;
            List<PersonaDTO> personas = new ArrayList();

            while ((linea = bufferedReader.readLine()) != null) {

                String id, nombre, email, direccion, genero, pais;
                Integer creditos;
                List<Double> geo = new ArrayList();
                Instant createdAt = null;
                String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
                String[] campos = linea.split(",");

                if (campos.length > 0) {
                    id = campos[0].trim();
                } else {
                    id = null;
                }
                if (campos.length > 1) {
                    nombre = campos[1].trim();
                } else {
                    nombre = null;
                }
                if (campos.length > 2 && campos[2].matches(emailRegex)) {
                    email = campos[2].trim();
                } else {
                    email = null;
                }
                if (campos.length > 3 && !campos[3].trim().isEmpty()) {
                    try {
                        creditos = Integer.valueOf(campos[3].trim());
                        if (creditos < 0) {
                            creditos = null;
                        }
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "EL campo creditos no tiene el frmato correcto en id:  {0}", id);
                        creditos = null;
                    }
                } else {
                    creditos = null;
                }
                if (campos.length > 4) {
                    direccion = campos[4].trim();
                } else {
                    direccion = null;
                }
                if (campos.length > 5 && campos.length > 6 && !campos[5].trim().isEmpty() && !campos[6].trim().isEmpty()) {
                    try {
                        geo.add(Double.valueOf(campos[5].trim()));
                        geo.add(Double.valueOf(campos[6].trim()));

                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Los campos geo no tienen el formato adecuado en : {0}", id);
                    }
                } else {
                    geo.add(null);
                }
                if (campos.length > 7 && (campos[7].equals("female") || campos[7].equals("male"))) {
                    genero = campos[7].trim();
                } else {
                    genero = null;
                }
                if (campos.length > 8 && !campos[8].trim().isEmpty()) {
                    pais = campos[8].trim();
                } else {
                    pais = null;
                }
                if (campos.length > 9 && !campos[9].trim().isEmpty()) {
                    try {
                        createdAt = Instant.parse(campos[9]);

                    } catch (DateTimeParseException e) {
                        logger.log(Level.SEVERE, "El campo ccreatesAt no tiene el formato adecuado en id: {0}", id);
                    }
                }

                PersonaDTO persona = new PersonaDTO();
                //Validar campo minimo y setear persona.
                if (id != null && nombre != null && email != null && creditos != null) {
                    persona.setId(id);
                    persona.setNombre(nombre);
                    persona.setEmail(email);
                    persona.setCreditos(creditos);
                    persona.setDireccion(direccion);
                    persona.setGeo(geo);
                    persona.setGenero(genero);
                    persona.setPais(pais);
                    persona.setCreatedAt(createdAt);
                    personas.add(persona);

                } else {
                    logger.log(Level.INFO, "Fila omitida por falta de ID, NOMBRE, EMAIL O CREDITOS. Linea N \u00b0:  {0}", linea);
                }
            }
            return personas;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "ERROR al procesar csv en PersonaServicio/procesarCSV: {0}", e);
            throw e;

        } finally {
            if (archivoTemporal.exists()) {
                archivoTemporal.delete();
            }
        }
    }

    /**
     * Recibe lista de PersonasDTO compara contra la API (FUENTE DE VERDAD) si
     * existe en la base de datos comparado por ID se ignora, Si existe en se
     * CRM API se actualiza y se guarda en la base de datos
     *
     * @param personasCSV
     */
    @Transactional
    public void sincronizarDatos(List<PersonaDTO> personasCSV) {

        for (PersonaDTO personaCsv : personasCSV) {

            try {
                // si esta en la base de datos se omite;
                Optional<Persona> respuesta = personaRepositorio.findById(personaCsv.getId());
                if (respuesta.isPresent()) {
                    logger.log(Level.INFO, "Persona Id:  {0} omitida ya existe en la base de datos.", personaCsv.getId());
                    continue;
                }

                String respuestaApi = crmApiServicio.getPersonaById(personaCsv.getId());

                if (respuestaApi != null) {

                    PersonaDTO personaApi = mapper.readValue(respuestaApi,
                            new TypeReference<PersonaDTO>() {
                    });

                    if (!personaCsv.getNombre().equals(personaApi.getNombre())) {
                        personaCsv.setNombre(personaApi.getNombre());
                    }
                    if (!personaCsv.getEmail().equals(personaApi.getEmail())) {
                        personaCsv.setEmail(personaApi.getEmail());
                    }
                    if (!personaCsv.getCreditos().equals(personaApi.getCreditos())) {
                        personaCsv.setCreditos(personaApi.getCreditos());
                    }
                    if (personaCsv.getDireccion() == null || !personaCsv.getDireccion().equals(personaApi.getDireccion())) {
                        personaCsv.setDireccion(personaApi.getDireccion());
                    } else {
                        personaCsv.setDireccion(null);
                    }
                    if (personaCsv.getGeo() == null || !personaCsv.getGeo().equals(personaApi.getGeo())) {
                        personaCsv.setGeo(personaApi.getGeo());
                    } else {
                        personaCsv.setGeo(null);
                    }
                    if (personaCsv.getGenero() == null || !personaCsv.getGenero().equals(personaApi.getGenero())) {
                        personaCsv.setGenero(personaApi.getGenero());
                    } else {
                        personaCsv.setGenero(null);
                    }
                    if (personaCsv.getPais() == null || !personaCsv.getPais().equals(personaApi.getPais())) {
                        personaCsv.setPais(personaApi.getPais());
                    } else {
                        personaCsv.setPais(null);
                    }
                    if (personaCsv.getCreatedAt() == null || !personaCsv.getCreatedAt().equals(personaApi.getCreatedAt())) {
                        personaCsv.setCreatedAt(personaApi.getCreatedAt());
                    } else {
                        personaCsv.setCreatedAt(null);
                    }
                    personaRepositorio.save(PersonaConvertidor.aEntidad(personaCsv));

                } else {
                    //Si no existe en la api ni en la base de datos se persiste sin cambios
                    personaRepositorio.save(PersonaConvertidor.aEntidad(personaCsv));
                }
            } catch (JsonProcessingException e) {
                //Error al procesar JSON
                logger.log(Level.SEVERE, "ERROR al procesar JSON para persona con ID:  {0}. ERROR: {1}",
                        new Object[]{personaCsv.getId(), e.getMessage()});

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error inesperado en psona con ID: {0}. ERROR {1}",
                        new Object[]{personaCsv.getId(), e.getMessage()});
            }
        }
    }

    /**
     *valida y genera una transferencia se persisten datos en transaccion
     * se persisten los cambios en las personas.
     * @param idEmisor
     * @param creditos
     * @param idReceptor
     * @throws Exception
     */
    @Transactional
    public void transferenciaCreditos(
            String idEmisor, Integer creditos, String idReceptor) throws Exception {

        if (creditos == null || creditos <= 0) {
            throw new IllegalArgumentException("ERROR: La cantidad de créditos debe ser mayor que cero.");
        }
        Optional<Persona> respuestaEmisor = personaRepositorio.findById(idEmisor);
        Optional<Persona> respuestaReceptor = personaRepositorio.findById(idReceptor);

        Persona emisor;
        Persona receptor;

        if (respuestaEmisor.isPresent()) {
            emisor = respuestaEmisor.get();
        } else {
            logger.log(Level.SEVERE, "EL emisor con ID: {} no existe en la Base de datos.", idEmisor);
            throw new IllegalArgumentException("ERROR el emisor no existe en la base de datos");
        }

        if (emisor.getCreditos() < creditos) {
            logger.log(Level.SEVERE, "EL emisor con ID: {} no posee creditos suficientes", idEmisor);
            throw new IllegalArgumentException("ERROR el emisor con ID: {0} no posee creditos suficientes");
        }

        if (respuestaReceptor.isPresent()) {
            receptor = respuestaReceptor.get();
            if (receptor.getCreditos() == null) {
                receptor.setCreditos(0);
            }

        } else {
            logger.log(Level.SEVERE, "EL receptor con ID: {} no existe en la Base de datos.", idReceptor);
            throw new IllegalArgumentException("ERROR el receptor no existe en la base de datos");
        }

        emisor.setCreditos(emisor.getCreditos() - creditos);
        receptor.setCreditos(receptor.getCreditos() + creditos);

        try {
            personaRepositorio.save(emisor);
            personaRepositorio.save(receptor);
            transaccionServicio.transaccionNueva(creditos);

        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "ERROR al guardar persona en base de datos", e);
            throw new RuntimeException("ERROR al persistir persona. ", e);

        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "ERROR al realizar la transaccion ");
            throw e;
        }

    }

}
