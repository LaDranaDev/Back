package mx.santander.monitoreoapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manejador global de excepciones del módulo de monitoreo.
 * <p>Traduce las excepciones de negocio y validación a respuestas HTTP consistentes para el front.</p>
 *
 * @author Rodrigo
 * R
 * P
 * M
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.debug.enabled:false}")
    private boolean debugEnabled;

    /**
     * M
     * a
     * n
     * e
     * j
     * o
     * d
     * e
     * e
     * r
     * r
     * o
     * r
     * e
     * s
     * 404 - Recurso no encontrado
     *
     * Se dispara cuando Spring no encuentra un handler para la ruta
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            WebRequest request) {

        // mensaje simple para el usuario
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message("The requested resource was not found")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Manejo de errores de validación
     *
     * Este métdo se encarga de procesar todos los errores cuando
     * algún campo no pasa las validaciones de Spring
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        // vamos a juntar todos los errores de validación en un mapa
        Map<String, String> errors = new HashMap<>();

        // recorremos cada error y lo agregamos con el nombre del campo
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // armamos la respuesta con toda la info útil para el front
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid input parameters")
                .validationErrors(errors)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        // regresamos el 400 con los detalles
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manejo de excepciones de negocio personalizadas
     *
     * Cuando algo sale mal en la lógica de negocio
     * lanzamos estas excepciones custom
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            WebRequest request) {

        // aquí simplemente pasamos el mensaje que ya viene
        // desde la capa de servicio con el status correcto
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Manejo de errores de acceso denegado
     *
     * Spring Security lanza estos cuando alguien intenta
     * acceder a un recurso sin los permisos necesarios
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request) {

        // mensaje genérico para no dar pistas de qué existe
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message("Access denied")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        // 403 directo, sin más detalles
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Manejo de excepciones genéricas - CRÍTICO PARA SEGURIDAD
     *
     * Este es el catch-all para cualquier error no esperado
     * Super importante no filtrar info sensible aquí
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        // generamos un ID único para poder rastrear en logs
        String errorId = UUID.randomUUID().toString();

        // guardamos odo el detalle en los logs del server
        // pero nada de esto va al cliente
        log.error("Error ID: {} - Exception: ", errorId, ex);

        // mensaje súper genérico para el usuario final
        String userMessage = "An internal error occurred. Please contact support with error ID: " + errorId;

        // solo en dev mostramos algo más de info
        // pero en prod jamás debe estar esto activo
        if (debugEnabled) {
            userMessage = String.format("%s - [DEBUG] %s", userMessage, ex.getMessage());
        }

        // armamos respuesta sin detalles comprometedores
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(userMessage)
                .errorId(errorId)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        // 500 genérico sin stack trace ni nada
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Clase para respuestas de error estructuradas
     *
     * Esta estructura la usamos para todos los errores
     * así el front siempre recibe el mismo formato
     */
    @lombok.Data
    @lombok.Builder
    public static class ErrorResponse {
        private LocalDateTime timestamp;  // cuándo pasó
        private int status;               // código HTTP
        private String error;             // tipo de error
        private String message;           // descripción para el usuario
        private String errorId;           // para rastrear en logs
        private String path;              // endpoint que falló
        private Map<String, String> validationErrors;  // solo para validaciones
    }

    /**
     * Excepción personalizada para errores de negocio
     *
     * La usamos cuando queremos controlar exactamente
     * qué mensaje y status devolver al cliente
     */
    public static class BusinessException extends RuntimeException {
        private final HttpStatus status;

        public BusinessException(String message, HttpStatus status) {
            super(message);
            this.status = status;
        }

        // getter necesario para el handler
        public HttpStatus getStatus() {
            return status;
        }
    }
}