// src/test/java/mx/santander/monitoreoapi/controller/PagoControllerResumenTest.java
package mx.santander.h2h.monitoreoapi.controller;

import mx.santander.h2h.monitoreoapi.model.request.PagoRequest;
import mx.santander.h2h.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.h2h.monitoreoapi.model.response.TotalesGlobalesDTO;
import mx.santander.h2h.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PagoControllerResumenTest {

    MockMvc mvc;
    IPagoService service;

    @BeforeEach
    void setup() {
        service = mock(IPagoService.class);
        mvc = MockMvcBuilders
                .standaloneSetup(new PagoController(service))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        var totales = new TotalesGlobalesDTO(10L, new BigDecimal("123.45"), Map.of("MXN", 7L, "USD", 3L));
        var resp = new DashboardResumenResponse(totales, List.of(), List.of(), List.of());
        when(service.obtenerDashboardResumen(any(PagoRequest.class))).thenReturn(resp);
    }

    @Test
    void resumen_ok_200() throws Exception {
        mvc.perform(post("/api/pagos/resumen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totales.totalPagos").value(10))
                .andExpect(jsonPath("$.totales.totalMontos").value(123.45))
                .andExpect(jsonPath("$.totales.pagosPorDivisa.MXN").value(7));
    }
}
