
package com.clientmanagement.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Rafael
 */
//Se registra en el contenedor  de  Spring
@Configuration
public class JacksonConfig {
    //@Bean maneja el ciclo de vida del modulo a travez de Spring
    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
          JavaTimeModule javaTimeModule = new JavaTimeModule();
        
         // Configuración adicional si el formato estándar no funciona
        mapper.registerModule(javaTimeModule);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")); // Ajusta el formato si es necesario
        return mapper;
    }
}
/*
Jackson y Java 8 Date/Time: Jackson, en su configuración básica, no incluye automáticamente
el conocimiento de cómo convertir una cadena de texto en formato ISO 8601 
jackson-datatype-jsr310: Este módulo proporciona a Jackson las herramientas necesarias para entender
y convertir esas cadenas de texto en instancias de las clases de fecha y hora de Java 8. Sin este módulo, 
Jackson trata esas clases como objetos no reconocidos y falla al intentar deserializarlas.
jsr310, le das a Jackson la capacidad de reconocer y manejar correctamente el formato de fecha y hora 
en las cadenas JSON, permitiendo así la deserialización correcta en tipos como 
LocalDateTime o OffsetDateTime.
*/
