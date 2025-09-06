package mx.santander.monitoreoapi.exception;

/**
 * Excepción de negocio usada cuando los filtros del request son inválidos o inconsistentes.
 * <p>Ejemplos: rango de fechas fuera de rango , {@code transactionId} no numérico, etc.</p>
 *
 * @author Rodrigo RPM
 * @since 1.0
 */
public class FiltroInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea una nueva excepción indicando la causa funcional del error de filtros.
     *
     * @param message descripción legible del problema encontrado en los filtros
     */
    public FiltroInvalidoException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción indicando la causa funcional y conservando la causa original.
     *
     * @param message descripción legible del problema encontrado en los filtros
     * @param cause   excepción raíz que originó el fallo (p. ej. {@link NumberFormatException})
     */
    public FiltroInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
