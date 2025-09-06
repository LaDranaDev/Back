// src/test/java/mx/santander/monitoreoapi/GlobalExceptionHandlerTest.java
package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.exception.FiltroInvalidoException;
import mx.santander.monitoreoapi.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Pruebas unitarias para {@link GlobalExceptionHandler}.
 * Objetivo:
 * - Validar que las excepciones personalizadas son manejadas correctamente
 *   y devuelven el formato esperado en la respuesta.
 * - Cubrir escenarios de filtros inválidos y errores de validación de campos.
 */
class GlobalExceptionHandlerTest {

    /**
     * Caso de prueba: manejar excepción {@link FiltroInvalidoException}.
     * Escenario:
     * - Se lanza una excepción indicando un rango de fechas inválido.
     * - El handler debe retornar un mapa con la estructura estándar de error.
     * - Se validan los valores de code, message, level y description.
     */
    @Test
    void handleFiltroInvalido_ok() {
        var geh = new GlobalExceptionHandler();

        Map<String, Object> body =
                geh.handleFiltroInvalido(new FiltroInvalidoException("Rango de fechas inválido"));

        // Verificaciones de contenido del mapa de error
        assertThat(body)
                .containsEntry("code", "FILTRO_INVALIDO")
                .containsEntry("message", "Rango de fechas inválido")
                .containsEntry("level", "warning")
                .containsEntry("description", "Revisa los filtros enviados.");
    }

    /**
     * Caso de prueba: manejar excepción {@link MethodArgumentNotValidException}.
     * Escenario:
     * - Se construye un BindingResult con un error de validación simulado.
     * - Se pasa a la excepción de validación.
     * - El handler debe retornar un ResponseEntity con estado 400 (BAD_REQUEST).
     * - El cuerpo de la respuesta debe contener una lista de errores
     *   con la estructura esperada.
     */
    @Test
    void handleValidationException_ok() {
        var geh = new GlobalExceptionHandler();

        // Construcción de un BindingResult con un error simulado
        var br = new BeanPropertyBindingResult(new Object(), "target");
        br.addError(new ObjectError("target", "Campo requerido"));

        // En Spring 6/Boot 3 el constructor requiere un MethodParameter
        MethodParameter mp = mock(MethodParameter.class);
        var ex = new MethodArgumentNotValidException(mp, br);

        // Ejecución del handler
        ResponseEntity<Map<String, Object>> resp = geh.handleValidationException(ex);

        // Verificación de estado HTTP
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Verificación del cuerpo
        assertThat(resp.getBody()).isNotNull();
        Object errors = resp.getBody().get("errors");
        assertThat(errors).isInstanceOf(List.class);

        // Validación del primer error de la lista
        @SuppressWarnings("unchecked")
        Map<String, Object> first =
                (Map<String, Object>) ((List<?>) errors).get(0);

        assertThat(first)
                .containsEntry("code", "VALIDATION_ERROR")
                .containsEntry("message", "Campo requerido")
                .containsEntry("level", "error");
    }
}
