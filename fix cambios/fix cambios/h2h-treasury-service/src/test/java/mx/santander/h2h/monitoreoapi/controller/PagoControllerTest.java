package mx.santander.h2h.monitoreoapi.controller;

import mx.santander.h2h.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.h2h.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.h2h.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PagoControllerTest.DataWebConfig.class) // <-- habilita Pageable en el slice MVC
class PagoControllerTest {

    @TestConfiguration
    @EnableSpringDataWebSupport // <-- registra PageableHandlerMethodArgumentResolver
    static class DataWebConfig {}

    @Autowired
    MockMvc mvc;

    @MockitoBean
    IPagoService pagoService;

    // JSON mínimo que cumple tus constraints (do opcional salvo formato)
    private static final String PAGO_REQUEST_JSON = """
      {
        "divisa":"MXN",
        "operacion":"EN",
        "tipoPago":"SPEI",
        "estatus":"EN",
        "referenciaCanal":"CANAL123"
      }
      """;

    @Test
    void resumen_dashboard_ok() throws Exception {
        Mockito.when(pagoService.obtenerDashboardResumen(any()))
                .thenReturn(Mockito.mock(DashboardResumenResponse.class));

        mvc.perform(post("/api/pagos/resumen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAGO_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void detalle_ok() throws Exception {
        Mockito.when(pagoService.obtenerDetalleConTotales(any(), any()))
                .thenReturn(Mockito.mock(PagoDetalleResponse.class));

        mvc.perform(post("/api/pagos/detalle")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAGO_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void totales_por_divisa_ok() throws Exception {
        Mockito.when(pagoService.sumaImportePorDivisaDetalle(any()))
                .thenReturn(List.of(Mockito.mock(ImporteTotalDivisaDTO.class)));

        mvc.perform(post("/api/pagos/detalle/totales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAGO_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
