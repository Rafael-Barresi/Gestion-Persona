package com.clientmanagement.conversores;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 *
 * @author Rafael
 */
public class CreditosDeserializador extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext dc) throws IOException, JacksonException {

        JsonNode nodo = p.getCodec().readTree(p);

  
        if (nodo.hasNonNull("créeditos")) {
            return convertirAInt(nodo.get("créditos"));
        }
        
        if (nodo.hasNonNull("creditos")) {
            return convertirAInt(nodo.get("creditos"));
        }
        return 0;
    }
    private Integer convertirAInt(JsonNode nodo) {
        try {
            if (nodo.isTextual()) {
                //Si el valor es un numero aeguramos que sea  un Integer
                return Integer.valueOf(nodo.asText().trim());

            } else if (nodo.isNumber()) {
                // Si el valor es un String  se intenta convertir a Integer
                return nodo.asInt();
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
        }
}
/*
JsonNode nodo = p.getCodec().readTree(p);
p.getCodec(): Obtiene el ObjectCodec del JsonParser, que es el objeto responsable de manejar la deserialización.

readTree(p): Usa el ObjectCodec para leer el árbol JSON del JsonParser y convertirlo en un JsonNode.

JsonNode: Es una estructura de datos que representa un nodo en el árbol JSON y permite acceder al 
valor del JSON de manera estructurada (puede ser un objeto, un array, un valor de texto, un número, etc
 */
