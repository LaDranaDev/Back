package mx.santander.monitoreoapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manejador global de excepciones del módulo de monitoreo.
 * <p>Traduce las excepciones de negocio y validación a respuestas HTTP consistentes para el front.</p>
 *ok
 * treshold del 25%
 * @author Rodrigo RPM
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de filtros inválidos del request.
     *
     * @param ex excepción de negocio con el detalle del filtro inválido
     * @return mapa con código y mensaje que el front puede mostrar al usuario
     */
    @ExceptionHandler(FiltroInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleFiltroInvalido(FiltroInvalidoException ex) {
        log.warn("Filtros inválidos: {}", ex.getMessage());
        return Map.of(
                "code", "FILTRO_INVALIDO",
                "message", ex.getMessage(),
                "level", "warning",
                "description", "Revisa los filtros enviados."
        );
    }

    /**
     * Maneja errores de validación a nivel de bean (anotaciones, @Valid).
     *
     * @param ex excepción de Spring con los errores de binding/validación
     * @return {@link ResponseEntity} con el cuerpo de error y estado 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errors", Collections.singletonList(
                Map.of(
                        "code", "VALIDATION_ERROR",
                        "message", Objects.requireNonNull(ex.getBindingResult()
                                .getAllErrors().get(0).getDefaultMessage()),
                        "level", "error",
                        "description", "Revisa los datos enviados."
                )
        ));
        return ResponseEntity.badRequest().body(error);
        /**
         * Maneja errores de validación a nivel de bean (anotaciones, @Valid).
         * cachando los
         *  posibles errores 
         * con el cuerpo de error y
         *  estado 400 (Bad Request)
         *  
         */
    }
}
