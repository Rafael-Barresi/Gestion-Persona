package com.clientmanagement.dto;

import com.clientmanagement.conversores.CreditosDeserializador;
import com.clientmanagement.conversores.GeoDeserializador;
import com.clientmanagement.conversores.InstantDeserializador;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import java.time.Instant;
import java.util.List;
/**
 * DTO para la entidad persona
 *
 * @author Rafael
 */
@JsonIgnoreProperties(ignoreUnknown = true)//Ignora campos no mapeados en JSON
public class PersonaDTO {

    private String id;
    
    private String nombre;
    
    private String email;
    
    //Venga con tipo String o null  se parsea a integer si es null va a ser 0.
    //@JsonDeserialize(using = CreditosDeserializador.class)
   @JsonAlias({"créditos", "creditos"}) // con esta anotacion reconoce ambos nombre de campo
    private Integer creditos;
    
    private String direccion;
   
    //Si el Double viene como String se transforma a Double si es un valor invalido sera null;
    @JsonDeserialize(using = GeoDeserializador.class)
    private List<Double> geo;
    
    private String genero;
    
    private String pais;
    
    //Deserializa a LocalDataTime
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @JsonDeserialize(using = InstantDeserializador.class)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP(3)")
    private Instant createdAt;
    
    public PersonaDTO() {

    }

    public PersonaDTO(String id, String nombre, String email, Integer creditos, String direccion
            , List<Double> geo, String genero, String pais, Instant createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.creditos = creditos;
        this.direccion = direccion;
        this.geo = geo;
        this.genero = genero;
        this.pais = pais;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Double> getGeo() {
        return geo;
    }

    public void setGeo(List<Double> geo) {
        this.geo = geo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

   


}
