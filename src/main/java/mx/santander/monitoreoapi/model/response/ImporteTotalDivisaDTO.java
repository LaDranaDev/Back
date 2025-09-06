package mx.santander.monitoreoapi.model.response;
/*
 * Proyecto: Monitoreo API
 * Archivo: PagoController.java
 * Descripción: Comentarios añadidos para documentar el propósito y funcionamiento del componente.
 * Autor: rrpm
 * Versión: 1.0
 * Fecha: 2025-09-02
 */


/**
 * DTO para mostrar el importe total agrupado por divisa.
 * Usado en la consulta {@code sumaImportePorDivisa}.
 *son comentarios
 * mas
 * comentarios
 * @param divisa código de la divisa (ej. MXN, USD)
 * @param importeTotal monto total para esa divisa
 */
/** DTO para el total por divisa. */
public record ImporteTotalDivisaDTO(
        String divisa,
        java.math.BigDecimal importeTotal
) implements java.io.Serializable {
    /** Filas de la página solicitada. */
    /** Filas de la página ss. */

    @java.io.Serial private static final long serialVersionUID = 1L;
}

