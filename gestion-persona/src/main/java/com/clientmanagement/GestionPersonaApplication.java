package com.clientmanagement;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionPersonaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionPersonaApplication.class, args);
        /*try {
            // Configurar el cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            Instant creado = Instant.now();

            // Crear el cuerpo de la solicitud con datos de prueba
            String requestBody = "{"
                + "\"id\": \"6\","
                + "\"nombre\": \"Modificado\","
                + "\"email\": \"modificado@ejemplo.com\","
                + "\"direccion\": \"Nueva Dirección 123\","
                + "\"geo\": [-18.5570, -49.7302],"
                + "\"genero\": \"male\","
                + "\"pais\": \"República Modificada\","
                + "\"créditos\": 199,"
                + "\"createdAt\": \"" + creado.toString() + "\""
                + "}";

            // Crear la solicitud POST con la codificación UTF-8
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprimir el código de estado de la respuesta
            System.out.println("Código de respuesta: " + response.statusCode());

            // Imprimir el cuerpo de la respuesta
            System.out.println("Cuerpo de la respuesta: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }*/
       /* try {
            // Configurar el cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Crear la solicitud DELETE
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://627303496b04786a09002b27.mockapi.io/mock/sucursales/6")) // URL con el ID específico
                    .DELETE() // Método DELETE
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprimir el código de estado de la respuesta
            System.out.println("Código de respuesta: " + response.statusCode());

            // Imprimir el cuerpo de la respuesta (si la API devuelve algo)
            System.out.println("Cuerpo de la respuesta: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
