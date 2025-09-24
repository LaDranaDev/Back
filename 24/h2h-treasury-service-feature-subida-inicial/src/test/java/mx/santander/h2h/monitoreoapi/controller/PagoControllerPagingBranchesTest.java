// src/test/java/mx/santander/monitoreoapi/controller/PagoControllerPagingBranchesTest.java
package mx.santander.h2h.monitoreoapi.controller;

import mx.santander.h2h.monitoreoapi.model.request.PagoRequest;
import mx.santander.h2h.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.h2h.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de paginación y ordenamiento para {@link PagoController}.
 * Objetivo:
 * - Cubrir ramas de lógica relacionadas con los parámetros page, size y sort
 *   que el controlador recibe y transforma en {@link Pageable}.
 * - Verificar clamping de page/size a valores permitidos y el comportamiento
 *   cuando se proporciona un ordenamiento (sort) en la petición.
 * Nota:
 * - Se utiliza {@link PageableHandlerMethodArgumentResolver} para habilitar el
 *   binding de Pageable en entorno standalone de MockMvc.
 */
class PagoControllerPagingBranchesTest {

    /** Simulador de peticiones HTTP al controlador */
    private MockMvc mvc;

    /** Servicio de pagos simulado */
    private IPagoService pagoService;

    /**
     * Configuración previa:
     * - Se mockea el servicio.
     * - Se construye el controlador y se habilita el resolver de Pageable.
     * - Se define una respuesta dummy para el servicio.
     */
    @BeforeEach
    void setup() {
        pagoService = mock(IPagoService.class);
        var controller = new PagoController(pagoService);

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                // Habilita el binding de Pageable para los métodos del controlador
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        when(pagoService.obtenerDetalleConTotales(any(PagoRequest.class), any(Pageable.class)))
                .thenReturn(dummyResponse());
    }

    /**
     * Construye una respuesta simulada con una fila y totales por divisa.
     */
    private PagoDetalleResponse dummyResponse() {
        var fila = new PagoDetalleDTO(
                "EN",                  // operacion
                "1234567890",          // cuentaAbono
                new BigDecimal("1.00"),// importe
                "REF-1",               // referenciaCanal
                "MXN",                 // divisa
                LocalDateTime.now(),   // fechaCarga
                "0987654321",          // cuentaCargo
                "SPEI",                // tipoPago
                "PROCESADO",           // estatus
                99                     // transactionId
        );
        Page<PagoDetalleDTO> page = new PageImpl<>(List.of(fila), PageRequest.of(0, 20), 1);
        var totales = List.of(new ImporteTotalDivisaDTO("MXN", new BigDecimal("1.00")));
        return new PagoDetalleResponse(page, totales);
    }

    /**
     * Caso: size=0 (inválido) → debe usarse el DEFAULT_PAGE_SIZE.
     */
    @Test
    void detalle_size_invalido_usa_DEFAULT() throws Exception {
        mvc.perform(post("/api/pagos/detalle?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(pagoService).obtenerDetalleConTotales(any(PagoRequest.class), cap.capture());

        Pageable used = cap.getValue();
        assertThat(used.getPageNumber()).isEqualTo(1);
        assertThat(used.getPageSize()).isEqualTo(20); // DEFAULT_PAGE_SIZE
    }

    /**
     * Caso: size mayor al máximo permitido → se debe "clamp" al MAX_PAGE_SIZE.
     */
    @Test
    void detalle_clampa_size_mayor_a_MAX() throws Exception {
        mvc.perform(post("/api/pagos/detalle?page=2&size=1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(pagoService, atLeastOnce()).obtenerDetalleConTotales(any(PagoRequest.class), cap.capture());

        Pageable used = cap.getValue();
        assertThat(used.getPageNumber()).isEqualTo(2);
        assertThat(used.getPageSize()).isEqualTo(100); // MAX_PAGE_SIZE
    }

    /**
     * Caso: page negativo → debe ajustarse a 0.
     */
    @Test
    void detalle_page_negativo_clampa_a_cero() throws Exception {
        mvc.perform(post("/api/pagos/detalle?page=-5&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(pagoService, atLeastOnce()).obtenerDetalleConTotales(any(PagoRequest.class), cap.capture());

        Pageable used = cap.getValue();
        assertThat(used.getPageNumber()).isZero(); // clamp
        assertThat(used.getPageSize()).isEqualTo(10);
    }

    /**
     * Caso: page/size válidos → no debe aplicarse clamping ni sort por defecto.
     */
    @Test
    void detalle_page_y_size_validos_no_clamp() throws Exception {
        mvc.perform(post("/api/pagos/detalle?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(pagoService).obtenerDetalleConTotales(any(PagoRequest.class), cap.capture());

        Pageable used = cap.getValue();
        assertThat(used.getPageNumber()).isEqualTo(1);
        assertThat(used.getPageSize()).isEqualTo(10);
        // Sin sort explícito, debe permanecer unsorted
        assertThat(used.getSort().isSorted()).isFalse();
    }

    /**
     * Caso: size dentro de rango permitido → no debe ajustarse.
     */
    @Test
    void detalle_size_normal_sin_clamp() throws Exception {
        mvc.perform(post("/api/pagos/detalle?page=3&size=25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(pagoService, atLeastOnce()).obtenerDetalleConTotales(any(PagoRequest.class), cap.capture());

        Pageable used = cap.getValue();
        assertThat(used.getPageNumber()).isEqualTo(3);
        assertThat(used.getPageSize()).isEqualTo(25); // sin cambios
        assertThat(used.getSort().isUnsorted()).isTrue();
    }

    /**
     * Caso: petición con sort → debe reflejarse en el Pageable capturado.
     */
    @Test
    void detalle_con_sort_cubre_rama_sort() throws Exception {
        mvc.perform(post("/api/pagos/detalle?page=0&size=5&sort=cuentaAbono,asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(pagoService, atLeastOnce()).obtenerDetalleConTotales(any(PagoRequest.class), cap.capture());

        Pageable used = cap.getValue();
        assertThat(used.getPageNumber()).isZero();
        assertThat(used.getPageSize()).isEqualTo(5);
        assertThat(used.getSort().isSorted()).isTrue();
        assertThat(used.getSort().getOrderFor("cuentaAbono")).isNotNull();
        assertThat(used.getSort().getOrderFor("cuentaAbono").isAscending()).isTrue();
    }
}
