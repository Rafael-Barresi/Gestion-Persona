package com.clientmanagement.conversores;

import com.clientmanagement.config.LogConfig;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class GeoDeserializador extends JsonDeserializer<List<Double>> {

    private static final Logger logger = LogConfig.getLogger(GeoDeserializador.class.getName());
    
    @Override
    public List<Double> deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JacksonException {
        //Entrada recibe un valor String con uno o mas valores separados por coma
        List<Double> coordenadas = new ArrayList();

            try {
                 JsonNode nodo = jp.getCodec().readTree(jp);
                
                    for (JsonNode elemento : nodo) {
                            //convertir String a double 
                            coordenadas.add(Double.parseDouble(elemento.asText()));
                    }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "ERROR al convertir a Double: ", e);
                //valores que no se pueden convertir a Double se ingresan como null;
                coordenadas.add(null);
            }
        return coordenadas;
    }
}