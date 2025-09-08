package mx.santander.monitoreoapi.model.response;



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

/*agregamos un comentario mas*/
/*agredagamos un comentario mas*/

