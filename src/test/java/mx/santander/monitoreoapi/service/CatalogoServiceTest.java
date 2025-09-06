package mx.santander.monitoreoapi.service;

import mx.santander.monitoreoapi.model.response.CatalogosResponse;
import mx.santander.monitoreoapi.model.response.OpcionCatalogo;
import mx.santander.monitoreoapi.repository.IDivisaRepository;
import mx.santander.monitoreoapi.repository.IStatusOperRepository;
import mx.santander.monitoreoapi.repository.ITipoOperacionRepository;
import mx.santander.monitoreoapi.repository.ITipoPagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CatalogoServiceTest {

    ITipoOperacionRepository tipoOperacionRepo;
    IDivisaRepository divisaRepo;
    ITipoPagoRepository tipoPagoRepo;
    IStatusOperRepository statusOperRepo;

    CatalogoService service;

    @BeforeEach
    void setup() {
        tipoOperacionRepo = mock(ITipoOperacionRepository.class);
        divisaRepo = mock(IDivisaRepository.class);
        tipoPagoRepo = mock(ITipoPagoRepository.class);
        statusOperRepo = mock(IStatusOperRepository.class);

        service = new CatalogoService(tipoOperacionRepo, divisaRepo, tipoPagoRepo, statusOperRepo);
    }

    @Test
    void obtenerCatalogosFiltros_ok() {
        // given
        when(tipoOperacionRepo.findAllAsOpcion())
                .thenReturn(List.of(new OpcionCatalogo("EN", "Envío")));
        when(divisaRepo.findAllAsOpcion())
                .thenReturn(List.of(new OpcionCatalogo("MXN", "Peso Mexicano")));
        when(tipoPagoRepo.findAllAsOpcion())
                .thenReturn(List.of(new OpcionCatalogo("SPEI", "Transferencia SPEI")));
        when(statusOperRepo.findAllAsOpcion())
                .thenReturn(List.of(new OpcionCatalogo("PROC", "Procesado")));

        // when
        CatalogosResponse r = service.obtenerCatalogosFiltros();

        // then
        assertThat(r).isNotNull();
        assertThat(r.getTiposOperacion()).containsExactly(new OpcionCatalogo("EN", "Envío"));
        assertThat(r.getDivisas()).containsExactly(new OpcionCatalogo("MXN", "Peso Mexicano"));
        assertThat(r.getTiposPago()).containsExactly(new OpcionCatalogo("SPEI", "Transferencia SPEI"));
        assertThat(r.getEstatus()).containsExactly(new OpcionCatalogo("PROC", "Procesado"));

        verify(tipoOperacionRepo).findAllAsOpcion();
        verify(divisaRepo).findAllAsOpcion();
        verify(tipoPagoRepo).findAllAsOpcion();
        verify(statusOperRepo).findAllAsOpcion();
        verifyNoMoreInteractions(tipoOperacionRepo, divisaRepo, tipoPagoRepo, statusOperRepo);
    }

    @Test
    void obtenerCatalogosFiltros_vacios_noNulos() {
        // given repos regresan listas vacías
        when(tipoOperacionRepo.findAllAsOpcion()).thenReturn(List.of());
        when(divisaRepo.findAllAsOpcion()).thenReturn(List.of());
        when(tipoPagoRepo.findAllAsOpcion()).thenReturn(List.of());
        when(statusOperRepo.findAllAsOpcion()).thenReturn(List.of());

        // when
        CatalogosResponse r = service.obtenerCatalogosFiltros();

        // then
        assertThat(r).isNotNull();
        assertThat(r.getTiposOperacion()).isEmpty();
        assertThat(r.getDivisas()).isEmpty();
        assertThat(r.getTiposPago()).isEmpty();
        assertThat(r.getEstatus()).isEmpty();
    }
}
