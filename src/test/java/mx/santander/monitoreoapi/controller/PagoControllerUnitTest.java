package mx.santander.monitoreoapi.controller;

import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.monitoreoapi.service.IPagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import static mx.santander.monitoreoapi.constants.QueryConstants.DEFAULT_PAGE_SIZE;
import static mx.santander.monitoreoapi.constants.QueryConstants.MAX_PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PagoControllerUnitTest {

    @Mock
    IPagoService pagoService;

    @InjectMocks
    PagoController controller;

    @Captor
    ArgumentCaptor<Pageable> pageableCaptor;
    @Test
    void obtenerDetallePaginado_fuerzaRamas_sizeCero_y_pageNegativo() {
        Mockito.when(pagoService.obtenerDetalleConTotales(any(), any()))
                .thenReturn(Mockito.mock(PagoDetalleResponse.class));

        PagoRequest filtro = new PagoRequest();

        // Pageable "inválido" a propósito (evita validaciones de PageRequest)
        Pageable invalido = new Pageable() {
            @Override public int getPageNumber() { return -5; }          // fuerza safePage < 0
            @Override public int getPageSize()   { return 0; }           // fuerza requestedSize <= 0
            @Override public long getOffset()    { return 0L; }
            @Override public Sort getSort()      { return Sort.unsorted(); }
            @Override public Pageable next() { return this; }
            @Override public Pageable previousOrFirst() { return this; }
            @Override public Pageable first() { return this; }
            @Override public Pageable withPage(int pageNumber) { return this; }
                @Override public boolean hasPrevious() { return false; }
        };

        ResponseEntity<PagoDetalleResponse> resp =
                controller.obtenerDetallePaginado(filtro, invalido);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();

        Mockito.verify(pagoService).obtenerDetalleConTotales(
                Mockito.eq(filtro),
                pageableCaptor.capture()
        );
        Pageable usado = pageableCaptor.getValue();

        int esperadoSize = Math.min(DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE);

        assertThat(usado.getPageNumber()).isZero();                 // normalizado a 0
        assertThat(usado.getPageSize()).isEqualTo(esperadoSize);    // default y recorte a MAX
    }

    @Test
    void obtenerDetallePaginado_sizeMayorQueMax_se_recorta_a_MAX_PAGE_SIZE() {
        Mockito.when(pagoService.obtenerDetalleConTotales(any(), any()))
                .thenReturn(Mockito.mock(PagoDetalleResponse.class));

        PagoRequest filtro = new PagoRequest();

        // page positivo para no pisar la otra rama; size exagerado para forzar Math.min(...)
        int hugeSize = MAX_PAGE_SIZE + 100;
        Pageable grande = PageRequest.of(2, hugeSize); // page=2 OK, size demasiado grande

        ResponseEntity<PagoDetalleResponse> resp =
                controller.obtenerDetallePaginado(filtro, grande);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();

        Mockito.verify(pagoService).obtenerDetalleConTotales(
                Mockito.eq(filtro),
                pageableCaptor.capture()
        );
        Pageable usado = pageableCaptor.getValue();

        // page debe mantenerse (no negativo), size debe recortarse a MAX_PAGE_SIZE
        assertThat(usado.getPageNumber()).isEqualTo(2);
        assertThat(usado.getPageSize()).isEqualTo(MAX_PAGE_SIZE);
    }

}
