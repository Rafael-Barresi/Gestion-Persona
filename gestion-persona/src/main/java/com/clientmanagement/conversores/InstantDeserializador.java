package com.clientmanagement.conversores;

import com.clientmanagement.config.LogConfig;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class InstantDeserializador extends JsonDeserializer<Instant> {

    private static final Logger logger = LogConfig.getLogger(InstantDeserializador.class.getName());

    @Override
    public Instant deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JacksonException {

        String dato = jp.getText();

        try {
            return Instant.parse(dato);
        } catch (DateTimeParseException e) {
            logger.log(Level.SEVERE, "ERROR al parsear  fecha: {0}", dato);
            throw new IOException("NO se proceso la fecha", e);
        }
    }
}
