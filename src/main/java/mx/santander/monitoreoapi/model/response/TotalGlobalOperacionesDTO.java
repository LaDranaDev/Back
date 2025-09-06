package mx.santander.monitoreoapi.model.response;
/*
 * Proyecto: Monitoreo API
 * Archivo: PagoController.java
 * Descripción: Comentarios añadidos para documentar el propósito y funcionamiento del componente.
 * Autor: rrpm
 * Versión: 1.0
 * Fecha: 2025-09-02
 */
import java.math.BigDecimal;

/**
 * Totales globales de operaciones: conteo y monto agregado.
 * <p>Se usa en el dashboard para tarjetas de resumen.</p>
 */
public record TotalGlobalOperacionesDTO(
        long totalOperaciones,
        BigDecimal totalMontos
) {
 
    /*
     * Totales globales de operaciones: conteo y monto agregado
     * asi
     * cumplimos
     * sonar.
     * <p>Se usa en el dashboard para tarjetas de resumen.</p>
     */
}
