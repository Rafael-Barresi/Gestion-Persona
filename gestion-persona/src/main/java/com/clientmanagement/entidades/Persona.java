package com.clientmanagement.entidades;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.time.Instant;
import java.util.List;

/**
 *
 * @author Rafael
 */
@Entity
public class Persona {

    @Id
    private String id;

    private String nombre;

    private String email;

    private Integer creditos;

    private String direccion;
    
    @ElementCollection
    @CollectionTable(name = "persona_geo", joinColumns = @JoinColumn(name = "persona_id"))
    @Column(name = "geo_value")
    private List<Double> geo;

    private String genero;

    private String pais;

    private Instant createdAt;

    public Persona() {

    }

    public Persona(String id, String nombre, String email, Integer creditos, String direccion, List<Double> geo, String genero, String pais, Instant createdAt) {
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
