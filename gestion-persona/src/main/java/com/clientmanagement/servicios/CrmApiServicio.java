package com.clientmanagement.servicios;

import com.clientmanagement.config.LogConfig;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        } catch (IOException e) {
            // errores de entrada/salida
            logger.log(Level.SEVERE, "IO Exepcion occurrida al buscar por id {0}: {1}", new Object[]{id, e.getMessage()});
            throw new Exception("Error al buscar persona poe ID.", e);
        }
    }

}
