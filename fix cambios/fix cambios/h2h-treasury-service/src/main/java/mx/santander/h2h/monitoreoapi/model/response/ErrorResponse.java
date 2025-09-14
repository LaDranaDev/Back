package mx.santander.h2h.monitoreoapi.model.response;



import java.io.Serializable;

/**
 * Representa un error retornado por el API.
 * Contiene código, mensaje, nivel y descripción detallada.
 */

public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Código único del error. */
    private String code;

    /** Mensaje corto para el usuario. */
    private String message;

    /** Nivel de severidad (INFO, WARN, ERROR). */
    private String level;

    /** Descripción detallada del error. */
    private String description;

    /**
     * Crea una nueva instancia de ErrorResponse.
     *
     * @param code código único del error
     * @param message mensaje corto para el usuario
     * @param level nivel de severidad
     * @param description descripción detallada
     */

    public ErrorResponse(String code, String message, String level, String description) {
        this.code = code;
        this.message = message;
        this.level = level;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }




}
