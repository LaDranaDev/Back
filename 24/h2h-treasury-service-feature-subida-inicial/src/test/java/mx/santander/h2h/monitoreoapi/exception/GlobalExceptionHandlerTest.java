package mx.santander.h2h.monitoreoapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler;
    WebRequest webRequest;

    @BeforeEach
    void setUp() throws Exception {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/demo");

        set(handler, "debugEnabled", false);
    }

    @Test
    void handleNoHandlerFoundException_retorna404()  {
        var ex = new NoHandlerFoundException("GET", "/missing", null);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> resp =
                handler.handleNoHandlerFoundException(ex, webRequest);

        assertThat(resp.getStatusCode().value()).isEqualTo(404);
        assertThat(resp.getBody().getError()).isEqualTo("Not Found");
        assertThat(resp.getBody().getPath()).isEqualTo("/api/demo");
    }

    @Test
    void handleBusinessException_usaStatusYMensaje() {
        var ex = new GlobalExceptionHandler.BusinessException(
                "Regla de negocio x",
                org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY);

        var resp = handler.handleBusinessException(ex, webRequest);

        assertThat(resp.getStatusCode().value()).isEqualTo(422);
        assertThat(resp.getBody().getMessage()).isEqualTo("Regla de negocio x");
    }

    @Test
    void handleAccessDeniedException_devuelve403() {
        var ex = new AccessDeniedException("nope");

        var resp = handler.handleAccessDeniedException(ex, webRequest);

        assertThat(resp.getStatusCode().value()).isEqualTo(403);
        assertThat(resp.getBody().getMessage()).isEqualTo("Access denied");
    }

    @Test
    void handleGlobalException_noExponeStackTrace_enProd() throws Exception {
        set(handler, "debugEnabled", false);
        var ex = new RuntimeException("Error de conexión a base de datos");

        var resp = handler.handleGlobalException(ex, webRequest);

        assertThat(resp.getStatusCode().value()).isEqualTo(500);
        assertThat(resp.getBody().getMessage())
                .startsWith("An internal error occurred. Please contact support with error ID:");
        assertThat(resp.getBody().getMessage()).doesNotContain("Error de conexión a base de datos");
        assertThat(resp.getBody().getErrorId()).isNotBlank();
    }

    @Test
    void handleGlobalException_muestraHintSoloEnDebug() throws Exception {
        set(handler, "debugEnabled", true);
        var ex = new RuntimeException("DB timeout");

        var resp = handler.handleGlobalException(ex, webRequest);

        assertThat(resp.getStatusCode().value()).isEqualTo(500);
        assertThat(resp.getBody().getMessage()).contains("[DEBUG] DB timeout");
    }

    private static void set(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
}
