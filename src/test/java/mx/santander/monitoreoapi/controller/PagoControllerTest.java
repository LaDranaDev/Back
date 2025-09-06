// src/test/java/mx/santander/monitoreoapi/controller/PagoControllerTest.java
package mx.santander.monitoreoapi.controller;

import mx.santander.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false) // desactiva Security filters
class PagoControllerTest {


    @Autowired
    MockMvc mvc;

    @MockitoBean
    IPagoService pagoService;

    // Ajusta este JSON si tu PagoRequest requiere campos obligatorios
    private static final String PAGO_REQUEST_JSON = """
        { "divisa":"MXN", "operacion":"EN" }
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
    void detalle_conPageYSize_ok() throws Exception {
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
    void detalle_sizeCero_yPaginaNegativa_usaSafeDefaults_ok() throws Exception {
        // Cubre ramas: requestedSize <= 0  y  safePage < 0
        Mockito.when(pagoService.obtenerDetalleConTotales(any(), any()))
                .thenReturn(Mockito.mock(PagoDetalleResponse.class));

        mvc.perform(post("/api/pagos/detalle")
                        .param("size", "0")
                        .param("page", "-5")
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
