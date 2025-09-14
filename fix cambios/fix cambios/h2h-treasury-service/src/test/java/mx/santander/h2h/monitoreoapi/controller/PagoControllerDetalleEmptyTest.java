package mx.santander.h2h.monitoreoapi.controller;

import mx.santander.h2h.monitoreoapi.exception.GlobalExceptionHandler;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.h2h.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PagoControllerDetalleEmptyTest {

    @Mock
    private IPagoService pagoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PagoController(pagoService))
                .setControllerAdvice(new GlobalExceptionHandler())
                // 👇 Necesario para que Spring pueda inyectar Pageable
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void detalle_vacio_ok_200() throws Exception {
        // given: page vacía y totales vacíos
        Page<PagoDetalleDTO> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);
        PagoDetalleResponse respuesta = new PagoDetalleResponse(page, Collections.emptyList());

        // Stub tolerante a los argumentos reales que recibe el controller
        given(pagoService.obtenerDetalleConTotales(any(), any()))
                .willReturn(respuesta);

        // when/then
        mockMvc.perform(
                post("/api/pagos/detalle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .content("{}")
        ).andExpect(status().isOk());
        // PagoDetalleResponse.content es transient (Page) y no se serializa.
    }
}
