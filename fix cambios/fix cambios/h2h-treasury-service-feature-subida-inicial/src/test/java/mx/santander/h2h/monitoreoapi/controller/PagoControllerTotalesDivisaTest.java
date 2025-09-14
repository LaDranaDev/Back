package mx.santander.h2h.monitoreoapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.santander.h2h.monitoreoapi.model.request.PagoRequest;
import mx.santander.h2h.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.h2h.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PagoControllerTotalesDivisaTest {

    @Mock
    IPagoService pagoService;

    MockMvc mvc;
    ObjectMapper om;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new PagoController(pagoService)).build();
        om = new ObjectMapper();
    }

    @Test
    void totalesPorDivisa_ok_200() throws Exception {
        // Mock: devuelve una lista con un total por divisa (nota: campo es importeTotal)
        List<ImporteTotalDivisaDTO> totales = List.of(
                new ImporteTotalDivisaDTO("MXN", new BigDecimal("123.45"))
        );
        when(pagoService.sumaImportePorDivisaDetalle(ArgumentMatchers.any(PagoRequest.class)))
                .thenReturn(totales);

        mvc.perform(post("/api/pagos/detalle/totales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].divisa").value("MXN"))
                // 👇 IMPORTANTE: el nombre correcto del campo es "importeTotal"
                .andExpect(jsonPath("$[0].importeTotal").value(123.45));
    }
}
