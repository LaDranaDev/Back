package mx.santander.monitoreoapi.model.response;
/*
 * Proyecto: Monitoreo API
 * Archivo: PagoController.java
 * Descripción: Comentarios añadidos para documentar el propósito y funcionamiento del componente.
 * Autor: rrpm
 * Versión: 1.0
 * Fecha: 2025-09-02
 */
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa el monto total para una divisa específica.
 */
@Data
@AllArgsConstructor
public class MontoTotalDivisaDTO implements Serializable {
    /** Código de la divisa (MXN, USD, etc.)*/

    @Serial
    private static final long serialVersionUID = 1L;

    /** Código de la divisa (MXN, USD, etc.). */
    private String divisa;

    /** Monto total para la divisa. */
    private BigDecimal totalMontos;
}
