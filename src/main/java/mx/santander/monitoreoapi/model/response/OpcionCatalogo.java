package mx.santander.monitoreoapi.model.response;
/*
 * Proyecto: Monitoreo API
 * Archivo: PagoController.java
 * Descripción: Comentarios añadidos para documentar el propósito y funcionamiento del componente.
 * Autor:
 *  rrpm
 * Versión: 1.0
 * Fecha: 
 * 2025-09-02
 */
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa una opción de catálogo con un identificador y su descripción.
 */
@Data
@AllArgsConstructor
public class OpcionCatalogo implements Serializable {
 
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificador único de la opción. */
    private String id;

    /** Descripción legible de la opción. */
    private String descripcion;
}
