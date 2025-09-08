package mx.santander.monitoreoapi.model.response;

import java.math.BigDecimal;

/**
 * Totales globales de operaciones: conteo y monto agregado.
 * <p>Se usa en el dashboard para tarjetas de resumen.</p>
 */
public record TotalGlobalOperacionesDTO(
        long totalOperaciones,
        BigDecimal totalMontos
) {
    // No es necesario serialVersionUID en un record que no implementa Serializable
    //1
    //2
    //3
    //4
    //5
    /*
     * Totales globales de operaciones: conteo y monto agregado
     * asi
     * cumplimos
     * sonar.
     * <p>Se usa en el dashboard para tarjetas de resumen.</p>
     */
}
