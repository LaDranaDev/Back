package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.controller.PagoController;
import mx.santander.monitoreoapi.exception.FiltroInvalidoException;
import mx.santander.monitoreoapi.exception.GlobalExceptionHandler;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;

/**
 * Pruebas unitarias para {@link PagoController}.
 * Objetivo:
 * - Validar el comportamiento de los endpoints de detalle y totales.
 * - Verificar que el controlador maneja correctamente respuestas exitosas
 *   y excepciones de filtros inválidos.
 * - Confirmar la integración con {@link IPagoService} mediante mocks.
 */
class PagoControllerTest {

    /** Utilidad para simular llamadas HTTP al controlador */
    private MockMvc mvc;

    /** Servicio de pagos simulado */
    private IPagoService pagoService;

    /**
     * Configuración inicial antes de cada prueba:
     * - Se mockea el servicio de pagos.
     * - Se construye el controlador con resolvers para paginación y sort.
     * - Se asocia el {@link GlobalExceptionHandler}.
     */
    @BeforeEach
    void setup() {
        pagoService = mock(IPagoService.class);
        PagoController controller = new PagoController(pagoService);

        // resolvers necesarios para Pageable y Sort en los endpoints
        var sortResolver = new SortHandlerMethodArgumentResolver();
        var pageableResolver = new PageableHandlerMethodArgumentResolver(sortResolver);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(pageableResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Caso de prueba: detalle de pagos con totales correcto.
     * Escenario:
     * - El servicio devuelve un {@link PagoDetalleResponse} con una página de resultados y totales.
     * - Se invoca el endpoint POST /api/pagos/detalle con filtros válidos.
     * - Se espera un HTTP 200 OK y JSON como respuesta.
     */
    @Test
    void detalle_ok() throws Exception {
        // Page<PagoDetalleDTO> dummy
        Page<PagoDetalleDTO> page = new PageImpl<>(
                List.of(new PagoDetalleDTO(
                        "EN", "123", BigDecimal.ONE, "REF", "MXN",
                        LocalDateTime.now(), "456", "SPEI", "PROCESADO", 99
                )),
                PageRequest.of(0,20),
                1
        );
        // Totales dummy
        List<ImporteTotalDivisaDTO> totales = List.of(new ImporteTotalDivisaDTO("MXN", BigDecimal.TEN));

        // Respuesta simulada por el servicio
        PagoDetalleResponse resp = new PagoDetalleResponse(page, totales);

        when(pagoService.obtenerDetalleConTotales(any(PagoRequest.class), any(Pageable.class)))
                .thenReturn(resp);

        // Cuerpo JSON de prueba
        String body = """
        {
          "operacion":"EN",
          "divisa":"MXN",
          "cuentaAbono":"123",
          "transactionId":"99",
          "fechaInicio":"2025-08-15",
          "fechaFin":"2025-08-16"
        }
        """;

        mvc.perform(post("/api/pagos/detalle?page=0&size=20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(pagoService).obtenerDetalleConTotales(any(PagoRequest.class), any(Pageable.class));
    }

    /**
     * Caso de prueba: detalle de pagos con filtros inválidos.
     * Escenario:
     * - El servicio lanza {@link FiltroInvalidoException}.
     * - Se invoca el endpoint POST /api/pagos/detalle con fechas inconsistentes.
     * - Se espera un HTTP 400 Bad Request.
     */
    @Test
    void detalle_errorFiltroInvalido() throws Exception {
        when(pagoService.obtenerDetalleConTotales(any(PagoRequest.class), any(Pageable.class)))
                .thenThrow(new FiltroInvalidoException("Rango de fechas inválido"));

        String body = """
        { "fechaInicio":"2025-08-16", "fechaFin":"2025-08-10" }
        """;

        mvc.perform(post("/api/pagos/detalle?page=0&size=20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    /**
     * Caso de prueba: totales de detalle de pagos.
     * Escenario:
     * - El servicio devuelve una lista de totales por divisa.
     * - Se invoca el endpoint POST /api/pagos/detalle/totales.
     * - Se espera un HTTP 200 OK con contenido JSON.
     */
    @Test
    void detalle_totales_ok() throws Exception {
        List<ImporteTotalDivisaDTO> totales = List.of(
                new ImporteTotalDivisaDTO("MXN", BigDecimal.ONE),
                new ImporteTotalDivisaDTO("USD", BigDecimal.TEN)
        );

        when(pagoService.sumaImportePorDivisaDetalle(any(PagoRequest.class)))
                .thenReturn(totales);

        String body = """
        { "divisa":"MXN", "fechaInicio":"2025-08-15", "fechaFin":"2025-08-16" }
        """;

        mvc.perform(post("/api/pagos/detalle/totales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(pagoService).sumaImportePorDivisaDetalle(any(PagoRequest.class));
    }
}
