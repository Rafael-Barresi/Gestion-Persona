package com.clientmanagement.servicios;

import com.clientmanagement.config.LogConfig;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rafael
 */
@Service
public class CrmApiServicio {

    //Instancia de clase que permite el envio de solicitudes HTTP y recibe respuesta de la web
    private final HttpClient httpClient;

    private static final Logger logger = LogConfig.getLogger(CrmApiServicio.class.getName());

    public CrmApiServicio() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Metodo para obtener la persona por id del CRM
     *
     * @param id
     * @return
     * @throws InterruptedException
     */
    public String getPersonaById(String id) throws Exception {

        if (id == null || id.isEmpty()) {
            logger.log(Level.WARNING, "El id es nulo o vacio.");
            throw new Exception("El ID no debe ser nulo");
        }

        try {

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales/" + id))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .GET()
                    .build();

            //Envia solicitud HTTP y recibe respuesta como un objeto HttpResponse que tiene el cuerpo de la 
            //respuesta en formato String.
            HttpResponse<String> respuesta = httpClient.send(peticion, HttpResponse.BodyHandlers.ofString());

            String respuestaBody = respuesta.body().trim();

            if (respuesta.body().contains("\"Not found\"")) {
                return null;
            } else {
                return respuestaBody;
            }

//            if (verificador.length > 1) {
//                return respuesta.body();
//            } else{
//                return null;
//            }
        } catch (IOException e) {
            // errores de entrada/salida
            logger.log(Level.SEVERE, "IO Exepcion occurrida al buscar por id {0}: {1}", new Object[]{id, e.getMessage()});
            throw new Exception("Error al buscar persona poe ID.", e);
        }
    }

    /**
     * Metodo que crea nueva persona
     *
     * @param requestBody
     * @return
     */
    public String crearPersona(String requestBody) {

        try {

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales/"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, HttpResponse.BodyHandlers.ofString());

            return respuesta.body();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Excepcion ocurrida en :  CREAR PERSONA ", e.getMessage());

        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupcion en la ejecucion de la solicitud. CREAR PERSONA", e.getMessage());
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * Actualiza personas exixtentes
     *
     * @param id
     * @param requestBody
     * @return
     */
    public String actualizarPersona(String email, String requestBody) {

        try {

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales/" + email))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, HttpResponse.BodyHandlers.ofString());

            return respuesta.body();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: en actualizar persona. IO Excepcion ocurrida: ", e.getMessage());

        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupcion en la ejecucion de la solicitud ACTUALIZAR PERSONA", e.getMessage());
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * Metodo para eliminar persona por id
     *
     * @param id
     * @return
     */
    public String eliminarPersona(String id) {

        try {

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, HttpResponse.BodyHandlers.ofString());

            return respuesta.body();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Excepcion ocurrida: ", e.getMessage());

        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupcion en la ejecucion de la solicitud", e.getMessage());
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "ERROR: ", e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve todos los registros de la api
     *
     * @return
     */
    public Optional<String> getAllPersonas() {

        try {

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales/"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .GET()
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                return Optional.of(respuesta.body());

            } else {
                logger.log(Level.WARNING, "Fallo la solicitud en getAllPersona {0}", respuesta.statusCode());
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Exepcion ocurrida al obtener todos los registros:", e.getMessage());

        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupcion en la ejecucion de la colicitud al obtener todos los registros", e.getMessage());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al obtener todos los registros", e.getMessage());
        }
        return Optional.empty();
    }

}
