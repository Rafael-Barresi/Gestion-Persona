
package com.clientmanagement.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 *
 * @author Rafael
 */
@Entity
public class Transaccion {
    
    @Id
    private String id;
    

    private LocalDateTime  fechaTransaccion;
    
    private Integer cantidadTransaccion;
    
    @PrePersist
    public void prePersist () {
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    
    public Transaccion () {
        
    }

    public Transaccion(String id, LocalDateTime fechaTransaccion, Integer cantidadTransaccion) {
        this.id = id;
        this.fechaTransaccion = fechaTransaccion;
        this.cantidadTransaccion = cantidadTransaccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Integer getCantidadTransaccion() {
        return cantidadTransaccion;
    }

    public void setCantidadTransaccion(Integer cantidadTransaccion) {
        this.cantidadTransaccion = cantidadTransaccion;
    }
}
